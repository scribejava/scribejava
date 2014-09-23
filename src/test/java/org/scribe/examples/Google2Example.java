package org.scribe.examples;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Google2Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class Google2Example {
	private static final String NETWORK_NAME = "Google OAuth2";
	private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
	private static final Token EMPTY_TOKEN = null;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		System.out.println("Enter your client id:");
		System.out.print(">>");
		String apiKey = in.nextLine();

		System.out.println("Enter your client secret:");
		System.out.print(">>");
		String apiSecret = in.nextLine();

		System.out
				.println("Enter a valid registered callback URL for your client ID:");
		System.out.print(">>");
		String callbackUrl = in.nextLine();

		OAuthService service = new ServiceBuilder()
				.provider(Google2Api.class)
				.apiKey(apiKey)
				.apiSecret(apiSecret)
				.scope("https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile")
				.callback(callbackUrl).build();

		System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
		System.out.println();

		// Obtain the Authorization URL
		System.out.println("Fetching the Authorization URL...");
		String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
		System.out.println("Got the Authorization URL!");
		System.out.println("Now go and authorize Scribe here:");
		System.out.println(authorizationUrl);
		System.out.println("And paste the authorization code here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		System.out.println();

		// Trade the Request Token and Verfier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: "
				+ accessToken + " )");
		System.out.println();

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		OAuthRequest request = new OAuthRequest(Verb.GET,
				PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.println(response.getCode());
		System.out.println(response.getBody());

		System.out.println();
		System.out
				.println("Thats it man! Go and build something awesome with Scribe! :)");
	}
}
