package org.scribe.examples;

import java.util.*;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class AsanaExample
{
  private static final String NETWORK_NAME = "Asana";
  private static final String PROTECTED_RESOURCE_URL = "https://app.asana.com/api/1.0/users/me";
  private static final String API_KEY = "your_api_key";
  private static final String API_SECRET = "your_api_secret";
  private static final String CALLBACK = "your_callback";
  private static final Token EMPTY_TOKEN = null;
  
  public static void main(String[] args)
  {
    OAuthService service = new ServiceBuilder()
                                  .provider(AsanaApi.class)
                                  .signatureType( SignatureType.QueryString )
                                  .apiKey(API_KEY)
                                  .apiSecret(API_SECRET)
                                  .callback(CALLBACK)
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

    // Access token has to be in the Authorizartion header for Asana's request
    // service.signRequest(accessToken, request);
    request.addHeader( "Authorization", "Bearer " + accessToken.getToken() );
    
    Response response = request.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getCode());
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");

  }
}