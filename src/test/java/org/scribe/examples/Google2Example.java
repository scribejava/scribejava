package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.builder.api.Google2Api;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

public class Google2Example
{
    private static final String NETWORK_NAME = "Google";
    private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
    private static final String SCOPE = "https://mail.google.com/ https://www.googleapis.com/auth/userinfo.email";
    private static final Token EMPTY_TOKEN = null;

    public static void main(String[] args)
  {
      // Client ID for web applications
      // String apiKey = "414137356658-u2q1rq7q0kc5p58h49liddetfgrgqaku.apps.googleusercontent.com";
      // String apiSecret = "5XZI3YRmHPvj6uuD-xrl_zDX";
      // String callbackUrl = "https://code.google.com/oauthplayground";
      // Client ID for installed applications (there is no secret)
      String apiKey = "414137356658.apps.googleusercontent.com";
      String apiSecret = "";
      String callbackUrl = "http://localhost";

      OAuthService service = new ServiceBuilder()
                                  .provider(Google2Api.class)
                                  .apiKey(apiKey)
                                  .apiSecret(apiSecret)
                                  .callback(callbackUrl)
                                  .scope(SCOPE)
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
    
    // Trade the Request Token and Verfier for the Access Token
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

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");

  }
}