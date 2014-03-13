package org.scribe.examples;

import java.util.Scanner;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.OdnoklassnikiApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class OdnoklassnikiExample {

    private static final String NETWORK_NAME = "Odnoklassniki.ru";
    private static final String PROTECTED_RESOURCE_URL
            = "http://api.odnoklassniki.ru/api/users/getCurrentUser?application_key=%1$s&format=JSON";
    private static final Token EMPTY_TOKEN = null;

    public static void main(String[] args) {
        // Replace these with your own api key and secret
        final String clientId = "your app id";
        final String publicKey = "your api secret";
        final String privateKey = "your public key";

        OAuthService service = new ServiceBuilder().provider(OdnoklassnikiApi.class)
                .apiKey(clientId)
                .apiSecret(privateKey)
                .scope("VALUABLE ACCESS")
                .callback("http://your.site.com/callback")
                .build();

        Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize SubScribe here:");
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
        OAuthRequest request = new OAuthRequest(Verb.GET, String.format(PROTECTED_RESOURCE_URL, publicKey));
        service.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with SubScribe! :)");
    }
}
