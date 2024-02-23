package com.github.scribejava.apis.examples;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.apis.ZendeskApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

@SuppressWarnings("PMD.SystemPrintln")
public class ZendeskExample {

    private static final String NETWORK_NAME = "Zendesk";
    
    public static void main(String... args) throws IOException, NoSuchAlgorithmException, KeyManagementException,
            InterruptedException, ExecutionException {
        
        // Replace these with your client id and secret
        final String clientId = "Your client ID";
        final String clientSecret = "Your client secret";
        final String clientUrl = "Your zendesk URL";
        final String callbackUrl = "Your callback URL";
        
        
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .callback(callbackUrl)
                .defaultScope("read")
                .build(ZendeskApi.instance(clientUrl));

        final Scanner in = new Scanner(System.in, "UTF-8");

        System.out.println("=== " + NETWORK_NAME + "'s OAuth 2.0 Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl();
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">> ");
        final String code = in.nextLine();
        System.out.println();

        // Trade the Authorization Code for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(code);
        System.out.println("Got the Access Token!");
        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, clientUrl + "/api/v2/users/me.json");
        service.signRequest(accessToken, request);
        try (Response response = service.execute(request)) {
            System.out.println("Got it! Lets see what we found...");
            System.out.println();
            System.out.println(response.getCode());
            System.out.println(response.getBody());
        }
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
        
    }

}
