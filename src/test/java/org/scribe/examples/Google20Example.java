package org.scribe.examples;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi20;
import org.scribe.examples.server.NanoHTTPD;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;

public class Google20Example
{
  private static final String NETWORK_NAME = "Google";
  private static final String SCOPE = "https://docs.google.com/feeds/";
  
  private static final String ROOT_URL = "https://www.googleapis.com";
  private static final String API_NAME = "drive";
  private static final String API_VERSION2 = "v2";
  private static final String URI_V2 = ROOT_URL + "/" + API_NAME + "/" + API_VERSION2;
  private static final String PROTECTED_RESOURCE_URL = URI_V2 + "/files";
  
  private static final Token EMPTY_TOKEN = null;

  private static final int port = 9080;
  private static final File wwwroot = new File(".").getAbsoluteFile();

  protected static NanoHTTPD httpd;
  
  public static void main(String[] args)
  {
    try {
      System.out.println("Starting local server");
      startLocalServer();
    
      // Replace these with your own api key and secret
      String apiKey = "186177184139.apps.googleusercontent.com";
      String apiSecret = "uVehkfuKPtzhO5VAXU-VEDVa";
      //String apiKey = "your api key";
      //String apiSecret = "your api secret";
      OAuthService service = new ServiceBuilder()
                                    .provider(GoogleApi20.class)
                                    .apiKey(apiKey)
                                    .apiSecret(apiSecret)
                                    .scope(SCOPE)
                                    .grantType(OAuthConstants.GRANT_TYPE_AUTHORIZATION_CODE)
                                    .accessType("offline")
                                    .callback("http://localhost:" + port + "/oauth2callback")
                                    .build();
      Scanner in = new Scanner(System.in);
  
      System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
      System.out.println();
  
      // Obtain the Authorization URL
      System.out.println("Fetching the Authorization URL...");
      String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
      System.out.println("Got the Authorization URL!");
      System.out.println("Now go and authorize Scribe here:");
      System.out.println(authorizationUrl);
      System.out.println("And paste the authorization code here");
      System.out.print(">>");
      Verifier verifier = new Verifier(in.nextLine());
      System.out.println();

      // Trade the Request Token and Verifier for the Access Token
      System.out.println("Trading the Request Token for an Access Token...");
      Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
      System.out.println("Got the Access Token!");
      System.out.println("(if your curious it looks like this: " + accessToken + " )");
      System.out.println();

      // Now let's go and ask for a protected resource!
      System.out.println("Now we're going to access a protected resource...");
      OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
      service.signRequest(accessToken, request);
      Response response = request.send();
      System.out.println("Got it! Lets see what we found...");
      System.out.println();
      System.out.println(response.getCode());
      System.out.println(response.getBody());

      // Trade the Request Token and Verifier for the Access Token
      System.out.println("Refreshing the Access Token...");
      String rawResponse = accessToken.getRawResponse();
      Pattern refreshTokenPattern = Pattern.compile("\"refresh_token\"\\s*:\\s*\"([^&\"]+)\"");
      Matcher matcher = refreshTokenPattern.matcher(rawResponse);
      if (matcher.find()) {
        String refreshToken = OAuthEncoder.decode(matcher.group(1));
        accessToken = service.refreshAccessToken(new Token(refreshToken, ""));
        System.out.println("Got the new Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println();
      }

      // Now let's go and ask for a protected resource!
      System.out.println("Now we're going to access a protected resource again...");
      request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
      service.signRequest(accessToken, request);
      response = request.send();
      System.out.println("Got it! Lets see what we found...");
      System.out.println();
      System.out.println(response.getCode());
      System.out.println(response.getBody());

      System.out.println();
      System.out.println("Thats it man! Go and build something awesome with Scribe! :)");

      System.out.println("Stopping local server");
      stopLocalServer();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private static void startLocalServer() throws IOException {
    httpd = new CustomNanoHTTPD(port, wwwroot);
  }

  private static void stopLocalServer() {
    httpd.stop();
  }

  
  protected static NanoHTTPD.Response listenRequest(NanoHTTPD server, String uri, String method, Properties header, Properties params, Properties files) {
    if (uri.endsWith("/oauth2callback")) {
      String code = params.getProperty("code");
      String oauthVerifier = params.getProperty("oauth_verifier");
      String msg = "<p>Authentication checked successfully: <ul><li>Code: " + code + "</li><li>OAuth Verifier: " + oauthVerifier + "</li></ul></p>";
      return server.new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, msg);
    }
    return null;
  }
  
  //---------------------------------------
  // Local HTTP Server
  // ---------------------------------------

  static final class CustomNanoHTTPD extends NanoHTTPD {

    public CustomNanoHTTPD(int port, File wwwroot) throws IOException {
      super(port, wwwroot);
    }

    @Override
    public NanoHTTPD.Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
      NanoHTTPD.Response response = listenRequest(this, uri, method, header, parms, files);
      return response != null ? response : super.serve(uri, method, header, parms, files);
    }
  }
}