package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TripItApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: madkrupt
 * Date: 9/2/13
 * Time: 3:14 PM
 */
public class TripItExample {
    private static final String NETWORK_NAME = "TripIt";
      private static final String PROTECTED_RESOURCE_URL = "https://api.tripit.com/v1/list/trip";

      public static void main(String[] args)
      {
        // Replace these with your own api key and secret
        String apiKey = "1b2ad305445067b8c74c77bbb53267d2f7a52394";
        String apiSecret = "e0583edecb039c1a8a65c2620deae66adf5ad48d";
        OAuthService service = new ServiceBuilder().provider(TripItApi.class).apiKey(apiKey).apiSecret(apiSecret).build();
        Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        Token requestToken = service.getRequestToken();
        System.out.println("Got the Request Token!");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        String authorizationUrl = service.getAuthorizationUrl(requestToken);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize Scribe here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
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
        //request.addBodyParameter("comment_id", "20100729223726:4fef610331ee46a3b5cbd740bf71313e");
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
