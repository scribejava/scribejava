package com.github.scribejava.apis.examples;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.apis.AirtableApi;
import com.github.scribejava.core.base64.Base64;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import com.github.scribejava.core.oauth.AuthorizationUrlBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;

public class AirtableExample {
    
    private static final String NETWORK_NAME = "Airtable";
    private static final String PROTECTED_RESOURCE_URL = "https://api.airtable.com/v0/meta/whoami";

    private AirtableExample() {
    }

    @SuppressWarnings("MPD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        //Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .callback("https://www.example.com/oauth_callback/")
                .build(AirtableApi.instance());
        final Scanner in = new Scanner(System.in, "UTF-8");

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        // Set a cryptographically generated string as the state
        // https://airtable.com/developers/web/api/oauth-reference#authorization-request-query
        SecureRandom random = new SecureRandom();
        final byte randomBytes[] = new byte[100];
        random.nextBytes(randomBytes);
        // at least one scope must be requested
        final String customScope = "data.records:read schema.bases:read";
        final AuthorizationUrlBuilder authorizationUrlBuilder = service.createAuthorizationUrlBuilder()
                .scope(customScope)
                .state(Base64.encodeUrlWithoutPadding(randomBytes))
                .initPKCE();
                
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJFava here:");
        System.out.println(authorizationUrlBuilder.build());
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final String code = in.nextLine();
        System.out.println();

        System.out.println("Trading the Authorization Code for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(AccessTokenRequestParams.create(code)
                .pkceCodeVerifier(authorizationUrlBuilder.getPkce().getCodeVerifier()));
        System.out.println("Got the Access Token!");
        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
        System.out.println();

        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);

        service.signRequest(accessToken, request);

        try (Response response = service.execute(request)) {
            System.out.println("Got it! Lets see what we found...");
            System.out.println();
            System.out.println(response.getCode());
            System.out.println(response.getBody());
        }

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
        
    }
}
