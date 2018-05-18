package com.github.scribejava.apis.examples;

import com.github.scribejava.httpclient.ning.NingHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;

public class FacebookAsyncNingExample {

    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/v2.11/me";

    private FacebookAsyncNingExample() {
    }

    public static void main(String... args) throws InterruptedException, ExecutionException, IOException {
        // Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";
        final String secretState = "secret" + new Random().nextInt(999_999);
        final NingHttpClientConfig clientConfig = new NingHttpClientConfig(new AsyncHttpClientConfig.Builder()
                .setMaxConnections(5)
                .setRequestTimeout(10_000)
                .setAllowPoolingConnections(false)
                .setPooledConnectionIdleTimeout(1_000)
                .setReadTimeout(1_000)
                .build());

        try (OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .state(secretState)
                .callback("http://www.example.com/oauth_callback/")
                .httpClientConfig(clientConfig)
                .build(FacebookApi.instance())) {
            final Scanner in = new Scanner(System.in, "UTF-8");

            System.out.println("=== " + NETWORK_NAME + "'s Async OAuth Workflow ===");
            System.out.println();

            // Obtain the Authorization URL
            System.out.println("Fetching the Authorization URL...");
            final String authorizationUrl = service.getAuthorizationUrl();
            System.out.println("Got the Authorization URL!");
            System.out.println("Now go and authorize ScribeJava here:");
            System.out.println(authorizationUrl);
            System.out.println("And paste the authorization code here");
            System.out.print(">>");
            final String code = in.nextLine();
            System.out.println();

            System.out.println("And paste the state from server here. We have set 'secretState'='"
                    + secretState + "'.");
            System.out.print(">>");
            final String value = in.nextLine();
            if (secretState.equals(value)) {
                System.out.println("State value does match!");
            } else {
                System.out.println("Ooops, state value does not match!");
                System.out.println("Expected = " + secretState);
                System.out.println("Got      = " + value);
                System.out.println();
            }

            // Trade the Request Token and Verfier for the Access Token
            System.out.println("Trading the Request Token for an Access Token...");
            final OAuth2AccessToken accessToken = service.getAccessTokenAsync(code).get();
            System.out.println("Got the Access Token!");
            System.out.println("(The raw response looks like this: " + accessToken.getRawResponse()
                    + "')");
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
}
