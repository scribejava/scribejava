package org.scribe.examples;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.KaixinApi;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.builder.api.SinaWeiboApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;


public class KaixinExample {

	private static String consumerKey = "563893638875a1b71d943932a053ca6b";
	private static String consumerSecret = "bbc71fb24d72dabdc2f8fb4a275dc5f3";

	private static final String NETWORK_NAME = "Kaixin";

	 
	  public static void main(String[] args)
	  {
	    OAuthService service = new ServiceBuilder()
	                                .provider(KaixinApi.class)
	                                .apiKey(consumerKey)
	                                .apiSecret(consumerSecret).signatureType(
	                						SignatureType.QueryString)
	                                .build();
	    Scanner in = new Scanner(System.in);
	    System.out.println("=== "+NETWORK_NAME+"'s OAuth Workflow ===");
	    System.out.println();

	    // Obtain the Request Token
	    System.out.println("Fetching the Request Token...");
	    Token requestToken = service.getRequestToken();
	    System.out.println("Got the Request Token!");
	    System.out.println();

	    System.out.println("Now go and authorize Scribe here:");
	    System.out.println(service.getAuthorizationUrl(requestToken));
	    System.out.println("And paste the verifier here");
	    System.out.print(">>");
	    Verifier verifier = new Verifier(in.nextLine());
	    System.out.println();

	    // Trade the Request Token and Verfier for the Access Token
	    System.out.println("Trading the Request Token for an Access Token...");
	    Token accessToken = service.getAccessToken(requestToken, verifier);
	    System.out.println("Got the Access Token!");
	    System.out.println("(if your curious it looks like this: " + accessToken + " )");
	    System.out.println();

	    // Other protected resource APIs can not be used util now!

	  }

}
