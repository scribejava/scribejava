package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.KeycloakApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class KeycloakExample {

    private KeycloakExample() {
    }

    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your own api key, secret, callback, base url and realm
        final String apiKey = "your_api_key";
        final String apiSecret = "your_api_secret";
        final String callback = "your_callback";
        final String baseUrl = "your_base_url";
        final String realm = "your_realm";

        final String protectedResourceUrl = baseUrl + "/auth/realms/" + realm + "/protocol/openid-connect/userinfo";

        final OAuth20Service service = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .scope("openid")
                .callback(callback)
                .build(KeycloakApi.instance(baseUrl, realm));
        final Scanner in = new Scanner(System.in);

        System.out.println("=== Keyloack's OAuth Workflow ===");
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
        final OAuthRequest request = new OAuthRequest(Verb.GET, protectedResourceUrl);
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
