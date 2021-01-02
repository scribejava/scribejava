package com.github.scribejava.apis.examples;

import java.util.Random;
import java.util.Scanner;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.ZohoApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Zoho20Example {

    private static final String NETWORK_NAME = "Zoho";
    private static final String PROTECTED_RESOURCE_URL = "https://accounts.zoho.com/oauth/user/info";

    private Zoho20Example() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {

        // Replace these with your client id and secret
        final String clientId = "your_client_id_here";
        final String clientSecret = "your_client_secret_here";
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .callback("https://someredirecturl")
                .build(ZohoApi20.instance());


        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();
        System.out.println("Fetching the Authorization URL...");

        // pass access_type=offline to get refresh token
        // https://www.zoho.com/accounts/protocol/oauth/web-apps/authorization.html
        final Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("access_type", "offline");
        //force to reget refresh token (if user are asked not the first time)
        additionalParams.put("prompt", "consent");

        final String secretState = "secret" + new Random().nextInt(999_999);

        final String authorizationUrl = service.createAuthorizationUrlBuilder()
                .scope("AaaServer.profile.READ")
                .state(secretState)
                .additionalParams(additionalParams)
                .build();


        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");

        try(Scanner in = new Scanner(System.in, "UTF-8")){
            final String code = in.nextLine();
            System.out.println();

            System.out.println("And paste the state from server here. "
                + "We have set 'secretState'='" + secretState + "'.");
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
            OAuth2AccessToken accessToken = service.getAccessToken(code);
            System.out.println("Got the Access Token!");
            System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");

            accessProtectedResource(service, accessToken);

            System.out.println("Refreshing the Access Token...");
            accessToken = service.refreshAccessToken(accessToken.getRefreshToken());
            System.out.println("Refreshed the Access Token!");
            System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
            System.out.println();

            accessProtectedResource(service, accessToken);
        }
    }

    @SuppressWarnings("PMD.SystemPrintln")
    private static void accessProtectedResource(OAuth20Service service,
    OAuth2AccessToken accessToken) throws IOException, InterruptedException, ExecutionException {
        // Now let's go and ask for a protected resource!
        System.out.println();
        System.out.println("Now we're going to access a protected resource...");

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
