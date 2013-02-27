package org.scribe.examples;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TradeStationLiveApi;
import org.scribe.builder.api.TradeStationSimulatedApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * @author John Jelinek IV
 */
public class TradesStationExample {
	private static final Token EMPTY_TOKEN = null;
	
	public static void main(String[] args) {
		simExample();
		liveExample();
	}

	private static void simExample() {
		// Replace these with your own api key and secret, environment API and example protected resource
		String apiKey = "your key goes here";
		String apiSecret = "your secret goes here";
		Class<TradeStationSimulatedApi> apiClass = TradeStationSimulatedApi.class;
		String protectedResource = "https://sim.api.tradestation.com/v2/data/quote/msft";
		
		example(apiKey, apiSecret, apiClass, protectedResource);
	}
	
	private static void liveExample() {
		//  Replace these with your own api key and secret, environment API and example protected resource
		String apiKey = "your key goes here";
		String apiSecret = "your secret goes here";
		Class<TradeStationLiveApi> apiClass = TradeStationLiveApi.class;
		String protectedResource = "https://api.tradestation.com/v2/data/quote/msft";

		example(apiKey, apiSecret, apiClass, protectedResource);
	}
	
	private static void example(String apiKey, String apiSecret, Class<? extends Api> apiClass, String protectedResource) {
		OAuthService service = new ServiceBuilder()
			.provider(apiClass)
			.apiKey(apiKey)
			.apiSecret(apiSecret)
			.callback("http://www.tradestation.com")
			.build();
		
		Verifier verifier = getAuthorizationUrl(service);
		Token accessToken = getAccessToken(service, verifier);
	    getProtectedResource(accessToken, protectedResource);
	}

	private static void getProtectedResource(Token accessToken, String protectedResource) {
		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
	    OAuthRequest request = new OAuthRequest(Verb.GET, protectedResource + "?oauth_token=" + accessToken.getToken());
	    Response response = request.send();
	    System.out.println("Got it! Lets see what we found...");
	    System.out.println();
	    System.out.println(response.getCode());
	    System.out.println(response.getBody());

	    System.out.println();
	    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
	}

	private static Token getAccessToken(OAuthService service, Verifier verifier) {
		// Trade the Request Token and Verifier for the Access Token
	    System.out.println("Trading the Request Token for an Access Token...");
	    Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
	    System.out.println("Got the Access Token!");
	    System.out.println("(if your curious it looks like this: " + accessToken + " )");
	    System.out.println();
		return accessToken;
	}

	private static Verifier getAuthorizationUrl(OAuthService service) {
		// Obtain the Authorization URL
		Scanner in = new Scanner(System.in);
	    System.out.println("Fetching the Authorization URL...");
	    String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
	    System.out.println("Got the Authorization URL!");
	    System.out.println("Now go and authorize Scribe here:");
	    System.out.println(authorizationUrl);
	    System.out.println("And paste the authorization code here");
	    System.out.print(">>");
	    Verifier verifier = new Verifier(in.nextLine());
	    in.close();
	    System.out.println();
		return verifier;
	}

}
