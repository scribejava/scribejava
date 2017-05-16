package org.scribe.examples;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.RedditApi;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class RedditExample {

	private static final String NETWORK_NAME = "Reddit";	
	
	public static void main(String[] args) {
		// Replace these with your own api key and secret
		String apiKey = "your-key";
		String apiSecret = "your-secret";

		//For sufficiently random + secure 'state' strings http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
		SecureRandom random = new SecureRandom();
		BigInteger randomInt = new BigInteger(128, random);
		
		OAuthConfig config = new OAuthConfig(apiKey, apiSecret, "your-callback", null, "identity", null);		
		
		RedditApi service = new RedditApi(true, randomInt.toString(32));
		
		Scanner in = new Scanner(System.in);
		
	    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
	    System.out.println();		

	    // Obtain the Authorization URL
	    System.out.println("Fetching the Authorization URL...");
	    String authorizationUrl = service.getAuthorizationUrl(config);
	    System.out.println("Got the Authorization URL!");
	    System.out.println("Now go and authorize Scribe here:");
	    System.out.println(authorizationUrl);
	    System.out.println("And paste the authorization code here");
	    System.out.print(">>");
	    Verifier verifier = new Verifier(in.nextLine());
	    System.out.println();	    
	}

}
