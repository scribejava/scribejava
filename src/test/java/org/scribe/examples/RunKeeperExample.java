
package org.scribe.examples;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

import java.util.Scanner;

public class RunKeeperExample
{
    private static final String CLIENT_ID = "****************************"; //Place your Client ID here
    private static final String CLIENT_SECRET = "*******************************"; //Place your Client Secret here
    private static final String PROTECTED_RESOURCE_URL = "http://api.runkeeper.com/fitnessActivities/";

    public static void main(String[] args)
    {
        OAuthService service = new ServiceBuilder()
                .provider(RunKeeperApi.class)
                .apiKey(CLIENT_ID)
                .apiSecret(CLIENT_SECRET)
                .callback("http://www.irasenthil.com")
                .build();

        Scanner in = new Scanner(System.in);

        System.out.println("=== RunKeeper OAuth Workflow ===");
        System.out.println();

        System.out.println("Go get the Authorization Code, Paste this URL in the browser and get the authorization when it redirect.");
        System.out.println(service.getAuthorizationUrl(null));
        System.out.println("And paste the Authorization Code here");
        System.out.print(">>");
        Verifier verifier = new Verifier(in.nextLine());
        System.out.println();

        // Trade the Authorization Code for the Access Token
        System.out.println("Trade the Authorization Code for the Access Token...");
        Token accessToken = service.getAccessToken(new Token(verifier.getValue(), null), verifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken.getToken() + " )");
        System.out.println();

        // Now let's go and ask for list of fitness activities!
        System.out.println("Now we're going to get list of fitness activities...");
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);

        request.addHeader("Accept", "*/*");
        request.addHeader("Content-Type", "application/vnd.com.runkeeper.NewFitnessActivity+json");
        service.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println("Here are the list of your fitness activities...");
        System.out.println();
        System.out.println(response.getBody());
    }
}
