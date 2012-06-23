package org.scribe.examples;

import java.util.*;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

/**
 * GitHub API Example using Scrible-Java
 * 
 */
public class GitHubExample 
{
	public static void main(String[] args) 
	{
		// Replace these with your own api key and secret
		String apiKey = "You GitHub client id";
		String apiSecret = "You Git client secret";
		OAuthService service = new ServiceBuilder().provider(GitHubApi.class)
												   										 .apiKey(apiKey)
												   										 .apiSecret(apiSecret)
												   										 .callback("You github's redirect uri")
												   										 .build();
		Scanner in = new Scanner(System.in);

		// Obtain the Authorization URL
		System.out.println("Fetching the Authorization URL...");
		String authorizationUrl = service.getAuthorizationUrl(null);
		System.out.println("Got the Authorization URL!");
		System.out.println("Now go and authorize Scribe here:");
		System.out.println(authorizationUrl);
		System.out.println("And paste the authorization code here");
		System.out.print(">>");
		String nextLine = "";

		nextLine = in.nextLine();
		Verifier verifier = new Verifier(nextLine);
		System.out.println();

		// Trade the Request Token and Verifier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(null, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: "+ accessToken + " )");
		System.out.println();

		OAuthRequest request = new OAuthRequest(Verb.GET,
				"https://api.github.com/users/YourGitHubAccount");
		service.signRequest(accessToken, request); // the access token from step 4
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.println(response.getCode());
		System.out.println(response.getBody());

		System.out.println();
		System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
	}
}
