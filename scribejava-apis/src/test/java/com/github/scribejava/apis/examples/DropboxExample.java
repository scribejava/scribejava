package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.DropboxApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class DropboxExample {

    private static final String NETWORK_NAME = "Dropbox.com";
    private static final String PROTECTED_RESOURCE_URL = "https://api.dropboxapi.com/2/users/get_space_usage";
    private static final String PAYLOAD = "null";
    private static final String CONTENT_TYPE_NAME = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";

    private DropboxExample() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your client id and secret
        final String clientId = "client id";
        final String clientSecret = "client secret";
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .callback("https://www.example.com/oauth_callback/")
                .build(DropboxApi.instance());

        final Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
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

        System.out.println("Trading the Authorization Code for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(code);
        System.out.println("Got the Access Token!");
        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
        System.out.println();

        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
        request.addHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        request.setPayload(PAYLOAD);
        service.signRequest(accessToken, request);

        try (Response response = service.execute(request)) {
            System.out.println("Got it! Lets see what we found...");
            System.out.println();
            System.out.println(response.getCode());
            System.out.println(response.getBody());
        }

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
    }
}
