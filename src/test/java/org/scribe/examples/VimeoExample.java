package org.scribe.examples;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.VimeoApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class VimeoExample {
	// replace with your API secret
	private static final String API_SECRET = "47e0d78390ea8191";
	
	// replace with your API key
	private static final String APIKEY = "9fa1ef0b08fba9a75f7614a9ebf856ce";

	// use out of bound for this example
	private static final String CALLBACK = "oob";

	private static final String AUTHORIZE_URL = "http://vimeo.com/oauth/authorize?oauth_token=%s&oauth_callback=%s&permission=delete";
	
	private final static String VIMEO_BASE = "http://vimeo.com/api/rest/v2?method=%s";
	
	// the login test resource requires oauth authentication
	private final static String VIMEO_METHOD = "vimeo.test.login";

	public static final String O_AUTH_VERSION = "1.0";

	public final static String VIMEO_ACCESS_TOKEN = "538984f70e15547899ee9ccd25c636aa";

	public final static String VIMEO_ACCESS_SECRET = "cc55f005c27f4677a4b6a202b85d27da31794c3b";

	public static final String SIGNATURE_METHOD = "HMAC-SHA1";


	public static void main(String[] args)
	{
	    Scanner in = new Scanner(System.in);
		OAuthService service = new ServiceBuilder()
		.provider(VimeoApi.class)
		.apiKey(APIKEY) // replace with your API key
		.apiSecret(API_SECRET) // replace with your API secret
		.callback(CALLBACK)
		.signatureType(SignatureType.QueryString)
		.build();

		System.out.println("Fetching the Request Token...");
		Token requestToken = service.getRequestToken();
		System.out.println("Got the Request Token!");

		String authUrl = String.format(AUTHORIZE_URL, requestToken.getToken(), CALLBACK);

		System.out.println("Now go and authorize Scribe here:");
		System.out.println(authUrl);
		System.out.println("And paste the verifier here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		System.out.println();

		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(requestToken, verifier);		
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: " + accessToken + " )");

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		String protectedResourceURL = String.format(VIMEO_BASE, VIMEO_METHOD);
		OAuthRequest orequest = new OAuthRequest(Verb.GET, protectedResourceURL);
		service.signRequest(accessToken, orequest);
		Response oresponse = orequest.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println(oresponse.getCode());
		System.out.println(oresponse.getBody());

		System.out.println();
	    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");

	}






}
