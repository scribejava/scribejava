package com.github.scribejava.apis.examples;

import java.util.Random;
import java.util.Scanner;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.model.AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuth20ServiceImpl;

public abstract class Google20Example {

    private static final String NETWORK_NAME = "G+";
    private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/plus/v1/people/me";

    public static void main(final String[] args) {
        final Scanner in = new Scanner(System.in, "UTF-8");

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();
        
        System.out.println("Enter your Google API client key");
        System.out.print(">>");
        final String clientId = in.nextLine();
        System.out.println("Enter your Google API client secret");
        System.out.print(">>");
        final String clientSecret = in.nextLine();
        final String secretState = "state_" + new Random().nextInt(999_999);
        final OAuth20ServiceImpl service = (OAuth20ServiceImpl) new ServiceBuilder()
                .provider(GoogleApi20.class)
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .scope("profile") // replace with desired scope
                .state(secretState)
                .build();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getOAuth2AuthorizationUrl();
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final Verifier verifier = new Verifier(in.nextLine());
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

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        OAuth2AccessToken accessToken = (OAuth2AccessToken)service.getOAuth2AccessToken(verifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if you're curious it looks like this: " + accessToken + " )");
        System.out.println("This token will expire in "+accessToken.getExpiresIn()+"ms");
        System.out.println();
        
        System.out.println("Refreshing the Access Token...");
        accessToken = (OAuth2AccessToken)service.refreshOAuth2AccessToken(accessToken);
        System.out.println("Refreshed the Access Token!");
        System.out.println("(if you're curious, the new one looks like this: " + accessToken + " )");
        System.out.println("This token will expire in "+accessToken.getExpiresIn()+"ms");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        while (true) {
            System.out.println("Paste fieldnames to fetch (leave empty to get profile, 'exit' to stop example)");
            System.out.print(">>");
            final String query = in.nextLine();
            System.out.println();

            final String requestUrl;
            if ("exit".equals(query)) {
                break;
            } else if (query == null || query.isEmpty()) {
                requestUrl = PROTECTED_RESOURCE_URL;
            } else {
                requestUrl = PROTECTED_RESOURCE_URL + "?fields=" + query;
            }

            final OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl, service);
            service.signRequest(accessToken, request);
            final Response response = request.send();
            System.out.println();
            System.out.println(response.getCode());
            System.out.println(response.getBody());

            System.out.println();
        }
    }
}
