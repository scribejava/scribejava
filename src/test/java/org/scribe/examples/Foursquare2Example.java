package org.scribe.examples;

import java.util.*;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class Foursquare2Example
{
  private static final String PROTECTED_RESOURCE_URL = "https://api.foursquare.com/v2/users/self/friends?oauth_token=";
  private static final Token EMPTY_TOKEN = null;

  public static void main(String[] args)
  {
    // Replace these with your own api key and secret
    String apiKey = "FEGFXJUFANVVDHVSNUAMUKTTXCP1AJQD53E33XKJ44YP1S4I";
    String apiSecret = "AYWKUL5SWPNC0CTQ202QXRUG2NLZYXMRA34ZSDW4AUYBG2RC";
    OAuthService service = new ServiceBuilder()
                                  .provider(Foursquare2Api.class)
                                  .apiKey(apiKey)
                                  .apiSecret(apiSecret)
                                  .callback("http://localhost:9000/")
                                  .build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== Foursquare2's OAuth Workflow ===");
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
    OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL + accessToken.getToken());
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
