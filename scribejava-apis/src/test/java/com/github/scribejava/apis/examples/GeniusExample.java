package com.github.scribejava.apis.examples;

import java.util.Scanner;

import com.github.scribejava.apis.GeniusApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

public abstract class GeniusExample {

	private static final String CLIENT_ID = "client_id";
	private static final String CLIENT_SECRET = "client_secret";
	private static final String REDIRECT_URI = "com.scribejavatest://callback";
	private static final String SCOPE = "me";
	private static final String STATE = "100";
	
	private static final String PROTECTED_RESOURCE_URL = "https://api.genius.com/songs/378195";
	
	public static void main(String... args) {
		
		final OAuth20Service service = new ServiceBuilder()
		    .apiKey(CLIENT_ID)
		    .apiSecret(CLIENT_SECRET)
		    .scope(SCOPE)
		    .state(STATE)
		    .callback(REDIRECT_URI)
		    .build(GeniusApi.instance());
		
		System.out.println("=== Genius' OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl();
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        
        Scanner in = new Scanner(System.in, "UTF-8"); 
        final String code = in.nextLine();
        System.out.println();

        System.out.println("And paste the state from server here. We have set 'secretState'='" + STATE + "'.");
        System.out.print(">>");
        final String value = in.nextLine();
        if (STATE.equals(value)) {
            System.out.println("State value does match!");
        } else {
            System.out.println("Ooops, state value does not match!");
            System.out.println("Expected = " + STATE);
            System.out.println("Got      = " + value);
            System.out.println();
        }
        
        // Trade the Request Token and Verifier for the Access Token        
        System.out.println("Trading the Request Token for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(code);
        System.out.println("Got the Access Token!");
        System.out.println("(View Access Token contents: " + accessToken
                + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Accessing a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        final Response response = request.send();
        System.out.println("Got it! Viewing contents...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
        in.close();
	}
}
