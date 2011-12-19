package org.scribe.examples;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.NkApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class NkExample {

    private static final String PROTECTED_RESOURCE_URL = "https://opensocial.nk-net.pl/v09/social/rest/people/@me?fields=age"; // More about resources you can find at http://developers.nk.pl/wiki/NK_OpenSocial_REST_API_documentation

    public static void main(String[] args) {

        // Replace these with your own api key and secret
        String apiKey = "scribekey";
        String apiSecret = "13a405fb-ac6d-4ffa-ad2c-156de265d1ad";
        String callback = "http://localhost:8080/oauth_callback/";
        String scope = "BASIC_PROFILE_ROLE,BIRTHDAY_PROFILE_ROLE"; // More about roles you can find at http://developers.nk.pl/wiki/Main_Page
        OAuthService service = new ServiceBuilder().provider(NkApi.class).apiKey(apiKey).apiSecret(apiSecret).callback(
                callback).scope(scope).build();
        Scanner in = new Scanner(System.in);

        System.out.println("=== NK's OAuth Workflow ===");
        System.out.println();
        System.out.println("Fetching the Authorization URL...");
        String authorizationUrl = service.getAuthorizationUrl(null);
        System.out.println("Got the Authorization URL!");
        System.out.println("Copy below url into your browser and authorize Scribe:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here. (http://localhost:8080/oauth_callback/?code=THERE_SHOULD_BE_YOUR_CODE)");
        System.out.print(">>");
        Verifier verifier = new Verifier(in.nextLine());
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        Token accessToken = service.getAccessToken(null, verifier);
        System.out.println("Got the Access Token!");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println("Code: " + response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it! Go and build something awesome with Scribe! :)");
    }
}