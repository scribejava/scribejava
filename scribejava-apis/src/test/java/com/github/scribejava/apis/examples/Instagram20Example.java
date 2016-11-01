package com.github.scribejava.apis.examples;

import java.io.IOException;
import java.util.Scanner;

import com.github.scribejava.apis.InstagramApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

public final class Instagram20Example {

    private static final String PROTECTED_RESOURCE_URL = "https://api.instagram.com/v1/users/self/?access_token=";

    private Instagram20Example() {
    }

    public static void main(String... args) throws IOException {
        // Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";
        final OAuth20Service service = new ServiceBuilder()
                .apiKey(clientId).apiSecret(clientSecret)
                .scope("basic") // replace with desired scope
                .callback("http://example.com/callback")
                .state("some_params")
                .build(InstagramApi20.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("=== Instragram's OAuth Workflow ===");
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

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(code);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken
                + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL + accessToken.getAccessToken(),
                service);
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
