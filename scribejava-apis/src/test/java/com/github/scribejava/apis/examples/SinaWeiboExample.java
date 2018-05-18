package com.github.scribejava.apis.examples;

import java.util.Scanner;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.SinaWeiboApi;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SinaWeiboExample {

    private static final String NETWORK_NAME = "SinaWeibo";
    private static final String PROTECTED_RESOURCE_URL = "http://api.t.sina.com.cn/account/verify_credentials.json";

    private SinaWeiboExample() {
    }

    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your own api key and secret
        final String apiKey = "your key";
        final String apiSecret = "your secret";
        final OAuth10aService service = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .build(SinaWeiboApi.instance());
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
        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service.signRequest(accessToken, request);
        final Response response = service.execute(request);
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
    }
}
