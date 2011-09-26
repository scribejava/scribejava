package org.scribe.examples;

import java.util.*;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class SinaWeiboExample
{
	private static final String NETWORK_NAME = "SinaWeibo";
	private static final String PROTECTED_RESOURCE_URL = "http://api.t.sina.com.cn/account/verify_credentials.json";

	public static void main(String[] args)
	{
		// Replace these with your own api key and secret
		String apiKey = "your key";
		String apiSecret = "your secret";
		OAuthService service = new ServiceBuilder()
				                        .provider(SinaWeiboApi.class)
				                        .apiKey(apiKey)
				                        .apiSecret(apiSecret)
				                        .build();
		Scanner in = new Scanner(System.in);

		System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
		System.out.println();

		// Grab a request token.
		System.out.println("Fetching request token.");
		Token requestToken = service.getRequestToken();
		System.out.println("Got it ... ");
		System.out.println(requestToken.getToken());

		// Obtain the Authorization URL
		System.out.println("Fetching the Authorization URL...");
		String authorizationUrl = service.getAuthorizationUrl(requestToken);
		System.out.println("Got the Authorization URL!");
		System.out.println("Now go and authorize Scribe here:");
		System.out.println(authorizationUrl);
		System.out.println("And paste the authorization code here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		System.out.println();

		// Trade the Request Token and Verfier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: "
				+ accessToken + " )");
		System.out.println();

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.println(response.getCode());
		System.out.println(response.getBody());

		System.out.println();
		System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
	}
}
