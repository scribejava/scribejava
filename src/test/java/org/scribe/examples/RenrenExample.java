package org.scribe.examples;

import java.util.Scanner;

import org.scribe.builder.api.RenrenApi;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;

public class RenrenExample {
	private static final String NETWORK_NAME = "Renren.com";
	private static final String PROTECTED_RESOURCE_URL = "https://graph.renren.com/renren_api/session_key?oauth_token=%s";

	public static void main(String[] args) {
		// Replace these with your own api key, secret and redirect_url 
		// the pair using blow is a public pair provided by renren.com 
		String apiKey = "fee11992a4ac4caabfca7800d233f814";
		String apiSecret = "a617e78710454b12aab68576382e8e14";
		String redirect_url = "http://graph.renren.com/oauth/login_success.html";
		OAuth20ServiceImpl service = new OAuth20ServiceImpl(new RenrenApi(), 
				new OAuthConfig(apiKey, apiSecret, redirect_url, SignatureType.Header, null)); 
		Scanner in = new Scanner(System.in);

		System.out.println("=== " + NETWORK_NAME + "'s OAuth2.0 Web Server Flow===");
		System.out.println();

		// Grab a Authorization Code.
		System.out.println("Fetching Authorization Code.");
		String authorizationUrl = service.getAuthorizationUrl(null);
		System.out.println("Got the Authorization URL!");
		System.out.println("Now go and authorize Scribe here:");
		System.out.println(authorizationUrl);
		System.out.println("Paste the url in your browser...");
		
		
		System.out.println("--------------------------------------------------------------------");
		System.out.println("Now Renren will redirect to 'http://graph.renren.com/oauth/login_success.html?code=lxK0A5Uh8LOOr21XEB3bU5cKWvdnFhND'");
		System.out.println("And copy the authorization code following 'code=', and paste it here");
		System.out.print(">>");
		Verifier authorization_code = new Verifier(in.nextLine());
		System.out.println("--------------------------------------------------------------------");

		// Trade the Authorization Code for the Access Token
		System.out.println("Trading the Authorization Code for an Access Token...");
		Token accessToken = service.getAccessToken(null, authorization_code);
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: "
				+ accessToken + " )");
		System.out.println("--------------------------------------------------------------------");

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		OAuthRequest request = new OAuthRequest(Verb.GET, String.format(PROTECTED_RESOURCE_URL, accessToken.getToken()));
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