package org.scribe.examples;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.BitlyApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class BitlyExample {
	private static final String NETWORK_NAME = "Bitly";

	private static final String PROTECTED_RESOURCE_URL = "https://api-ssl.bitly.com/v3/expand?shortUrl=" + encode("http://bit.ly/Lt5SJo");

	private static final Token EMPTY_TOKEN = null;

	public static void main(String[] args) {

		String apiKey = "your_app_id";
		String apiSecret = "your_api_secret";

		OAuthService service = new ServiceBuilder() //
				.provider(BitlyApi.class) //
				.apiKey(apiKey) //
				.apiSecret(apiSecret) //
				.callback("http://localhost:8080/oauth_callback") //
				.debug() //
				.build();
		Scanner in = new Scanner(System.in);

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
		System.out.println("(if your curious it looks like this: " + accessToken + " )");
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

	/**
	 * need this because using urlencoder directly for the field will make the
	 * compiler complain about an uncaught exception, like a runtime exception
	 * is any better, just fail fast and don't make such a fuss about it
	 * @param url unencoded url
	 * @return urlencoded url (utf-8)
	 */
	private static final String encode(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
