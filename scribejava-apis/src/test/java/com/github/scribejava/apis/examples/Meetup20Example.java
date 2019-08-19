package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.MeetupApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Meetup20Example {

    private static final String NETWORK_NAME = "Meetup";
    private static final String PROTECTED_RESOURCE_URL = " https://api.meetup.com/self/groups";

    private Meetup20Example() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your Meetup consumer id and secret
        final String consumerKey = "your consumer key";
        final String consumerSecret = "your consumer secret";
        final OAuth20Service service = new ServiceBuilder(consumerKey)
                .apiSecret(consumerSecret)
                .defaultScope("basic") // replace with desired scopes
                .callback("http://example.com/callback") // replace with appropriate URI for your consumer,
                // see https://www.meetup.com/meetup_api/auth/#redirect_uris
                .build(MeetupApi20.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String secretState = "secret" + new Random().nextInt(999_999);
        final String authorizationUrl = service.getAuthorizationUrl(secretState);
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

        System.out.println("Now we're going to the user's groups....");

        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service.signRequest(accessToken, request);
        System.out.println();
        try (Response response = service.execute(request)) {
            System.out.println(response.getCode());
            System.out.println(response.getBody());
        }

        System.out.println();
    }
}
