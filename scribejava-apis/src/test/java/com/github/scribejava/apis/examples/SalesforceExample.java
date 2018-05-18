package com.github.scribejava.apis.examples;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Scanner;

import com.github.scribejava.apis.SalesforceApi;
import com.github.scribejava.apis.salesforce.SalesforceToken;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class SalesforceExample {

    private static final String NETWORK_NAME = "Salesforce";

    private SalesforceExample() {
    }

    public static void main(String... args) throws IOException, NoSuchAlgorithmException, KeyManagementException,
            InterruptedException, ExecutionException {
        // Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";
        //IT's important! Salesforce upper require TLS v1.1 or 1.2.
        //They are enabled in Java 8 by default, but not in Java 7
        SalesforceApi.initTLSv11orUpper();

        // The below used ServiceBuilder connects to login.salesforce.com
        // (production environment).
        //
        // When you plan to connect to a Sandbox environment you've to use SalesforceApi.sandbox() API instance
        // new ServiceBuilder.....build(SalesforceApi.sandbox());

        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .callback("https://www.example.com/callback")
                .build(SalesforceApi.instance());

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
        final String code;
        try (Scanner in = new Scanner(System.in)) {
            code = in.nextLine();
        }
        System.out.println();

        // The code needs to be URL decoded
        final String codeEncoded = URLDecoder.decode(code, "UTF-8");

        // Trade the Request Token and Verifier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");

        final OAuth2AccessToken accessToken = service.getAccessToken(codeEncoded);
        final SalesforceToken salesforceAccessToken;
        if (accessToken instanceof SalesforceToken) {
            salesforceAccessToken = (SalesforceToken) accessToken;
        } else {
            throw new IllegalStateException("Salesforce API didn't return SalesforceToken.");
        }
        System.out.println("Got the Access Token!");

        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
        System.out.println();

        System.out.println("instance_url is: " + salesforceAccessToken.getInstanceUrl());

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're reading accounts from the Salesforce org (maxing them to 10).");

        // Sample SOQL statement
        final String queryEncoded = URLEncoder.encode("Select Id, Name from Account LIMIT 10", "UTF-8");

        // Building the query URI. We've parsed the instance URL from the accessToken request.
        final String url = salesforceAccessToken.getInstanceUrl() + "/services/data/v36.0/query?q=" + queryEncoded;

        System.out.println();
        System.out.println("Full URL: " + url);

        final OAuthRequest request = new OAuthRequest(Verb.GET, url);
        service.signRequest(salesforceAccessToken, request);
        final Response response = service.execute(request);
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());
    }
}
