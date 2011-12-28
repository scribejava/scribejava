package org.scribe.examples;

import com.google.gson.*;
import java.util.*;
import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

// https://developers.google.com/+/api/
// https://developers.google.com/+/api/latest/people/get#try-it
// http://code.google.com/apis/accounts/docs/OAuth2WebServer.html#callinganapi

// https://code.google.com/apis/console/#project:419708088990:quotas
// https://code.google.com/apis/console/#project:419708088990:access
// http://sites.google.com/site/gson/gson-user-guide#TOC-Overview

public class GooglePlusExample{
	
	 public static void main(String args[]) throws Exception {

		String apiKey = "419708088990.apps.googleusercontent.com";
		String secret = "tNpXobmvQ3tM2234BW2OsjXT";
		String callback = "http://www.r-chart.com";
		String scope = "https://www.googleapis.com/auth/plus.me";
		
		OAuthService service = new ServiceBuilder()
		            .provider(GooglePlusApi.class)
		            .apiKey(apiKey)
		            .apiSecret(secret)
		            .callback(callback)
		            .scope(scope)
		            .build();
		
		Scanner in = new Scanner(System.in);

		System.out.println("Fetching the Authorization URL..."); 
		String authorizationUrl = service.getAuthorizationUrl(null);
		System.out.println("Got the Authorization URL!");
     	System.out.println("Now go and authorize Scribe here:");
     	System.out.println(authorizationUrl);
    	System.out.println("And paste the authorization code here");
   	System.out.print(">>");
  		String code = in.nextLine();

  		Verifier verifier = new Verifier(code);
		System.out.println("Trading the Request Token for an Access Token...");

		Token accessToken = service.getAccessToken(null, verifier);

		System.out.println("Now we're going to access a protected resource...creating request");
		String url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken.getToken();
		System.out.println("Trying userInfo url: " + url);
		OAuthRequest request = new OAuthRequest(Verb.GET, url);

		System.out.println("Sending request... getting response");
		  
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.println(response.getCode());
      String b = response.getBody();
     	System.out.println(b);

    	String id = extractId(b);
   	System.out.println("Sending request... getting response");
    	request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/plus/v1/people/me?access_token=" + accessToken.getToken());
      response = request.send();
      System.out.println("Got it! Lets see what we found...");
     	System.out.println(response.getCode());
     	System.out.println(response.getBody());

	   }

		public static String extractId(String body) {
		        JsonParser parser = new JsonParser();
		        JsonElement e = parser.parse(body);
		        return e.getAsJsonObject().get("id").getAsString();
		}

}