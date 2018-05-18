package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.PinterestApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class PinterestExample {

    private static final String PROTECTED_RESOURCE_URL = "https://api.pinterest.com/v1/me/";

    private PinterestExample() {
    }

    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your own api key and secret
        final String apiKey = "your_app_id";
        final String apiSecret = "your_app_secret";
        final OAuth20Service service = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .scope("read_public,write_public,read_relationships,write_relationships")
                .callback("https://localhost/") // Add as valid callback in developer portal
                .build(PinterestApi.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("=== Pinterest's OAuth Workflow ===");
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
