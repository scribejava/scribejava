package com.github.scribejava.apis.examples;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import com.github.scribejava.apis.InstagramApi;
import com.github.scribejava.apis.instagram.InstagramService;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

public class InstagramExample {

    private static final String NETWORK_NAME = "Instagram";
    private static final String PROTECTED_RESOURCE_URL
            = "https://graph.instagram.com/me/media?fields=id,caption,media_type,media_url,username";

    private InstagramExample() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your client id and secret
        final String clientId = "client-id";
        final String clientSecret = "client-secret";
        final String secretState = "secret" + new Random().nextInt(999_999);
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .defaultScope("user_profile,user_media")
                .callback("http://example.com")
                .build(InstagramApi.instance());

        final Scanner in = new Scanner(System.in, "UTF-8");

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl(secretState);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final String code = in.nextLine();
        System.out.println();

        System.out.println("And paste the state from server here. We have set 'secretState'='" + secretState + "'.");
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

        System.out.println("Trading the Authorization Code for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(code);
        System.out.println("Got the Access Token!");
        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service.signRequest(accessToken, request);
        try (Response response = service.execute(request)) {
            System.out.println("Got it! Lets see what we found...");
            System.out.println();
            System.out.println(response.getCode());
            System.out.println(response.getBody());
        }
        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");

        final InstagramService instagramService = (InstagramService) service;
        System.out.println("Now let's exchange our short-lived token to long-lived access token...");
        final OAuth2AccessToken longLivedAccessToken = instagramService.getLongLivedAccessToken(accessToken);
        System.out.println("Got the Access Token!");
        System.out.println("(The access token raw response looks like this: " + longLivedAccessToken.getRawResponse()
                + "')");

        System.out.println("Now it's time to refresh long-lived token...");
        final OAuth2AccessToken refreshAccessToken = service.refreshAccessToken(longLivedAccessToken.getAccessToken());
        System.out.println("Got the refreshed Access Token!");
        System.out.println("(The refreshed access token raw response looks like this: "
                + refreshAccessToken.getRawResponse() + "')");
    }
}
