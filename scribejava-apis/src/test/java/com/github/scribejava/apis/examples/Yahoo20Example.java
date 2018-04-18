package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.YahooApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Flow as documented at https://developer.yahoo.com/oauth2/guide/flows_authcode
 * <ol>
 * <li>Create an application at https://developer.yahoo.com/apps/create/</li>
 * <li>Make sure application has permission to API resource (Profiles, Fantasy Sports, etc)</li>
 * <li>get Client ID and Client Secret after registering your app</li>
 * </ol>
 */
public class Yahoo20Example {

    private static final String PROTECTED_RESOURCE_URL
            = "https://fantasysports.yahooapis.com/fantasy/v2/users;use_login=1/games/teams";

    private Yahoo20Example() {
    }

    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Add your personal information here
        final String clientId = "ADD CLIENT ID HERE!!!!";
        final String clientSecret = "ADD SECRET HERE!!!!";

        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .callback(OAuthConstants.OOB)
                .build(YahooApi20.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("=== Yahoo's OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        System.out.println("Got the Request Token!");
        System.out.println();

        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(service.getAuthorizationUrl());
        System.out.println("And paste the verifier here");
        System.out.print(">>");
        final String oauthVerifier = in.nextLine();
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(oauthVerifier);
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
