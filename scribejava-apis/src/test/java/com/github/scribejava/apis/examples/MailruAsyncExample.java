package com.github.scribejava.apis.examples;

import com.ning.http.client.AsyncHttpClientConfig;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import com.github.scribejava.apis.MailruApi;
import com.github.scribejava.core.builder.ServiceBuilderAsync;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;

public abstract class MailruAsyncExample {

    private static final String NETWORK_NAME = "Mail.ru";
    private static final String PROTECTED_RESOURCE_URL = "http://www.appsmail.ru/platform/api?method=users.getInfo&secure=1";
    private static final Token EMPTY_TOKEN = null;

    public static void main(final String... args) throws InterruptedException, ExecutionException {
        // Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";

        final AsyncHttpClientConfig clientConfig = new AsyncHttpClientConfig.Builder()
                .setMaxConnections(5)
                .setRequestTimeout(10000)
                .setAllowPoolingConnections(false)
                .setPooledConnectionIdleTimeout(1000)
                .setReadTimeout(1000)
                .build();

        final OAuthService service = new ServiceBuilderAsync()
                .provider(MailruApi.class)
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .callback("http://www.example.com/oauth_callback/")
                .asyncHttpClientConfig(clientConfig)
                .build();

        final Scanner in = new Scanner(System.in, "UTF-8");

        System.out.println("=== " + NETWORK_NAME + "'s Async OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final Verifier verifier = new Verifier(in.nextLine());
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final Token accessToken = service.getAccessTokenAsync(EMPTY_TOKEN, verifier, null).get();
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println();

        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequestAsync request = new OAuthRequestAsync(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        final Response response = request.sendAsync(null).get();

        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
        service.closeAsyncClient();
    }
}
