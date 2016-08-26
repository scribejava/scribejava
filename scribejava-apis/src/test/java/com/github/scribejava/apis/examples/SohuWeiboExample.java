package com.github.scribejava.apis.examples;

import java.util.Scanner;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.SohuWeiboApi;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import java.io.IOException;

public final class SohuWeiboExample {

    private static final String NETWORK_NAME = "SohuWeibo";
    private static final String PROTECTED_RESOURCE_URL = "http://api.t.sohu.com/account/verify_credentials.json";

    private SohuWeiboExample() {
    }

    public static void main(String... args) throws IOException {
        // Replace these with your own api key and secret
        final String apiKey = "your_key";
        final String apiSecret = "your_secret";
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build(SohuWeiboApi.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Grab a request token.
        System.out.println("Fetching request token.");
        final OAuth1RequestToken requestToken = service.getRequestToken();
        System.out.println("Got it ... ");
        System.out.println(requestToken.getToken());

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl(requestToken);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final String oauthVerifier = in.nextLine();
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken
                + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        final Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
    }
}
