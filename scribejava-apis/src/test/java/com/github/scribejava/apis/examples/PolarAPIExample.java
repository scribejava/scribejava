package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.PolarAPI;
import com.github.scribejava.apis.polar.PolarOAuth2AccessToken;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.util.Scanner;

public class PolarAPIExample {

    private static final String NETWORK_NAME = "Polar";

    private static final String PROTECTED_RESOURCE_URL = "https://www.polaraccesslink.com/v3/users/%s";

    private PolarAPIExample() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws Exception {
        final String clientId = "your_api_client";
        final String clientSecret = "your_api_secret";
        final String scope = "accesslink.read_all";
        final String callback = "your_api_callback";
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .defaultScope(scope)
                //your callback URL to store and handle the authorization code sent by Polar
                .callback(callback)
                .build(PolarAPI.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl("some_params");
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final String code = in.nextLine();
        System.out.println();

        System.out.println("Trading the Authorization Code for an Access Token...");
        System.out.println("Got the Access Token!");
        final OAuth2AccessToken oauth2AccessToken = service.getAccessToken(code);
        System.out.println("(if your curious it looks like this: " + oauth2AccessToken
                + ", 'rawResponse'='" + oauth2AccessToken.getRawResponse() + "')");
        System.out.println();

        if (!(oauth2AccessToken instanceof PolarOAuth2AccessToken)) {
            System.out.println("oauth2AccessToken is not instance of PolarOAuth2AccessToken. Strange enough. exit.");
            return;
        }

        final PolarOAuth2AccessToken accessToken = (PolarOAuth2AccessToken) oauth2AccessToken;

        // Now let's go and ask for a protected resource!
        // This will get the profile for this user
        System.out.println("Now we're going to access a protected resource...");

        final OAuthRequest request = new OAuthRequest(Verb.GET, String.format(PROTECTED_RESOURCE_URL,
                accessToken.getUserId()));
        request.addHeader("Accept", "application/json");

        service.signRequest(accessToken, request);

        System.out.println();
        try (Response response = service.execute(request)) {
            System.out.println(response.getCode());
            System.out.println(response.getBody());
        }
        System.out.println();
    }
}
