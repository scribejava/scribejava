package com.github.scribejava.apis.examples;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.SalesforceApi;
import com.github.scribejava.apis.salesforce.SalesforceToken;
import com.github.scribejava.core.model.ForceTypeOfHttpRequest;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.ScribeJavaConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.ning.http.client.AsyncHttpClientConfig;

public class SalesforceAsyncExample {

	private static final String NETWORK_NAME = "Salesforce";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// Replace these with your client id and secret
		final String clientId = "your client id";
		final String clientSecret = "your client secret";
		
        ScribeJavaConfig.setForceTypeOfHttpRequests(ForceTypeOfHttpRequest.FORCE_ASYNC_ONLY_HTTP_REQUESTS);
        final AsyncHttpClientConfig clientConfig = new AsyncHttpClientConfig.Builder()
                .setMaxConnections(5)
                .setRequestTimeout(10_000)
                .setAllowPoolingConnections(false)
                .setPooledConnectionIdleTimeout(1_000)
                .setReadTimeout(10_000)
                .build();
		
		final OAuth20Service service = new ServiceBuilder()
				.apiKey(clientId)
				.apiSecret(clientSecret)
				.asyncHttpClientConfig(clientConfig)
				.callback("https://www.example.com/callback") // Salesforce only accepts secured callbacks
				.build(SalesforceApi.instance());
		final Scanner in = new Scanner(System.in);

		System.out.println("=== " + NETWORK_NAME + "'s OAuth20 Workflow ===");
		System.out.println();

		// Obtain the Authorization URL
		System.out.println("Fetching the Authorization URL...");
		final String authorizationUrl = service.getAuthorizationUrl();
		System.out.println("Got the Authorization URL!");
		System.out.println("Now go and authorize ScribeJava here:");
		System.out.println(authorizationUrl);
		System.out.println("And paste the authorization code here");
		System.out.print(">>");
		final String code = in.nextLine();
		System.out.println();
		in.close();

		String codeEncoded = code;

		// The code needs to be URL decoded
		try {
			codeEncoded = URLDecoder.decode(code, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// Trade the Request Token and Verifier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		final SalesforceToken accessToken = (SalesforceToken) service.getAccessTokenAsync(codeEncoded, null).get();
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: " + accessToken
				+ ", 'rawResponse'='" + accessToken.getRawResponse() + "')");
		System.out.println();

		System.out.println("Instance is: " + accessToken.getInstanceUrl());

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're reading accounts from the Salesforce org (maxing them to 10).");

		// Sample SOQL statement
		String queryEncoded = "Select Id, Name from Account LIMIT 10";

		try {
			queryEncoded = URLEncoder.encode(queryEncoded, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			queryEncoded = "";
		}

		if (!queryEncoded.isEmpty()) {
			// Building the query URI. We've parsed the instance URL from the accessToken request.
			String url = accessToken.getInstanceUrl() + "/services/data/v36.0/query?q=" + queryEncoded;

			System.out.println();
			System.out.println("Full URL: " + url);

			final OAuthRequestAsync request = new OAuthRequestAsync(Verb.GET, url, service);
			request.addHeader("Authorization", "Bearer " + accessToken.getAccessToken());
			request.sendAsync(new OAuthAsyncRequestCallback() {

				@Override
				public void onThrowable(Throwable t) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onCompleted(Object response) {
					System.out.println();
					System.out.println(((Response)response).getCode());
					System.out.println(((Response)response).getBody());
				}
			});
		}


	}
}
