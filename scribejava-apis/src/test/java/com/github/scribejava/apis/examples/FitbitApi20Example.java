package com.github.scribejava.apis.examples;
import java.util.Scanner;

import com.github.scribejava.apis.FitbitApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * Created by hd on 10/09/16.
 */
public class FitbitApi20Example {

    private static final String NETWORK_NAME = "Fitbit";
    
    // Replace with user ID to test API against
    private static final String USER_ID = "your user id";
    private static final String PROTECTED_RESOURCE_URL = "https://api.fitbit.com/1/user/" + USER_ID + "/profile.json";

  
    private FitbitApi20Example() {
    }

    public static void main(String... args) throws Exception {
    	
        // Replace these with your client id and secret fron your app
        final String clientId = "your client id";
        final String clientSecret = "your client secret";
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .scope("activity profile") // replace with desired scope
                .callback("http://www.example.com/oauth_callback/")  //your callback URL to store and handle the authorization code sent by Fitbit
                .state("some_params")
                .build(FitbitApi20.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
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
        System.out.println("(if your curious it looks like this: " + accessToken
                + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        // This will get the profile for this user
        System.out.println("Now we're going to access a protected resource...");

        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        request.addHeader("x-li-format", "json");

        //add header for authentication (why make it so complicated, Fitbit?)
        request.addHeader("Authorization", "Bearer " + accessToken.getAccessToken());

        final Response response = service.execute(request);
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
    }
}
