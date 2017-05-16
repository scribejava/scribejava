package org.scribe.examples;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.RenrenMobileApi;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class RenrenMobileExample {
	private static final String NETWORK_NAME = "Renren";
	private static final String PROTECTED_RESOURCE_URL = "http://api.m.renren.com/api";
	private static final Token EMPTY_TOKEN = null;

	public static void main(String[] args) {
		// Replace these with your own api key and secret
		String apiKey = "your api key";
		String apiSecret = "your api secret";
		OAuthService service = new ServiceBuilder().provider(RenrenMobileApi.class)
				.apiKey(apiKey).apiSecret(apiSecret)
				.callback("http://your.doman.com/oauth/renren")
				.scope("phoneclient.toolLog")
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
		OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL + "/profile/getInfo");
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("call_id", ""+System.currentTimeMillis()); 
		parameters.put(OAuthConstants.ACCESS_TOKEN, accessToken.getToken()); 
		parameters.put("format", "json");
		parameters.put("v", "1.0");

		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			request.addQuerystringParameter(entry.getKey(), entry.getValue());
		}
		request.addQuerystringParameter("sig", signature(parameters, apiSecret));
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.println(response.getCode());
		System.out.println(response.getBody());

		System.out.println();
		System.out.println("Thats it man! Go and build something awesome with Scribe! :)");

	}

	public static String signature(TreeMap<String, String> map, String secretKey) {
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> e = it.next();
			String key = e.getKey();
			String value = e.getValue();
			sb.append(key);
			sb.append("=");
			if (value.length() > 50) {
				value = value.substring(0, 50);
			}
			sb.append(value);
		}
		sb.append(secretKey);
		String sig = md5(sb.toString());
		return sig;
	}
	
	public static String md5(String orgString) {
		try {
			java.security.MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(orgString.getBytes(Charset
					.forName("UTF-8")));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
