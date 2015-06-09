package ru.hh.oauth.subscribe.apis.examples;

import java.util.Scanner;
import ru.hh.oauth.subscribe.core.builder.ServiceBuilder;
import ru.hh.oauth.subscribe.apis.SohuWeiboApi;
import ru.hh.oauth.subscribe.core.model.OAuthRequest;
import ru.hh.oauth.subscribe.core.model.Response;
import ru.hh.oauth.subscribe.core.model.Token;
import ru.hh.oauth.subscribe.core.model.Verb;
import ru.hh.oauth.subscribe.core.model.Verifier;
import ru.hh.oauth.subscribe.core.oauth.OAuthService;

public class SohuWeiboExample {

    private static final String NETWORK_NAME = "SohuWeibo";
    private static final String PROTECTED_RESOURCE_URL = "http://api.t.sohu.com/account/verify_credentials.json";

    public static void main(String[] args) {
        // Replace these with your own api key and secret
        String apiKey = "your_key";
        String apiSecret = "your_secret";
        OAuthService service = new ServiceBuilder()
                .provider(SohuWeiboApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();
        Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Grab a request token.
        System.out.println("Fetching request token.");
        Token requestToken = service.getRequestToken();
        System.out.println("Got it ... ");
        System.out.println(requestToken.getToken());

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        String authorizationUrl = service.getAuthorizationUrl(requestToken);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize SubScribe here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        Verifier verifier = new Verifier(in.nextLine());
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        Token accessToken = service.getAccessToken(requestToken, verifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: "
                + accessToken + " )");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
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
