package org.scribe.examples;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.NetflixApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class NetflixExample 
{
  private static final String NETWORK_NAME = "Netflix";
  private static final String PROTECTED_RESOURCE_URL = "http://api.netflix.com/users/current";
  private static final Map<String , String> KEYS = new HashMap<String , String>() {{
    put("oauth_consumer_key", "848fk40vfkslr90vfkskl4dklf9d0kNOTVALID");
  }};
	  
  public static void main(String[] args)
  {
    OAuthService service = new ServiceBuilder()
                                .provider(NetflixApi.class)
                                .apiKey("848fk40vfkslr90vfkskl4dklf9d0kNOTVALID")
                                .apiSecret("Ju67as9UYINOTVALID")
                                .build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
    System.out.println();

    // Obtain the Request Token
    System.out.println("Fetching the Request Token...");
    Token requestToken = service.getRequestToken();
    System.out.println("Got the Request Token!");
    System.out.println();

    System.out.println("Now go and authorize Scribe here:");
    System.out.println(service.getAuthorizationUrl(requestToken, KEYS));
    System.out.println("And paste the verifier here");
    System.out.print(">>");
    Verifier verifier = new Verifier(in.nextLine());
    System.out.println();

    // Trade the Request Token and Verfier for the Access Token
    System.out.println("Trading the Request Token for an Access Token...");
    Token accessToken = service.getAccessToken(requestToken, verifier);
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
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
  }

}
