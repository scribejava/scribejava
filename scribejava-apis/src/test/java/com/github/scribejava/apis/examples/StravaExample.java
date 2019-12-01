package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.StravaApi;
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

public class StravaExample {

    private static final String NETWORK_NAME = "Strava";
    private static final String ATHLETE_INFO = "https://www.strava.com/api/v3/athlete";

    private StravaExample() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";

        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .defaultScope("read_all") // replace with desired scope
                .callback("http://localhost/callback") // use registered origin callback here
                .build(StravaApi.instance());
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

        // Now let's get athlete info
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, ATHLETE_INFO);
        service.signRequest(accessToken, request);
        try (Response response = service.execute(request)) {
            System.out.println("Got it! Lets see what we found...");
            System.out.println();
            System.out.println(response.getCode());
            System.out.println(response.getBody());
        }

        System.out.println();
        System.out.println("That's it man! Go and build something awesome with ScribeJava! :)");
    }
}
