package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class FlickrExample {

    private static final String PROTECTED_RESOURCE_URL = "http://api.flickr.com/services/rest/";

    private FlickrExample() {
    }

    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your own api key and secret
        final String apiKey = "your_app_id";
        final String apiSecret = "your_api_secret";

        final OAuth10aService service = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .build(FlickrApi.instance(FlickrApi.FlickrPerm.DELETE));
        final Scanner in = new Scanner(System.in);

        System.out.println("=== Flickr's OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        final OAuth1RequestToken requestToken = service.getRequestToken();
        System.out.println("Got the Request Token!");
        System.out.println();

        System.out.println("Now go and authorize ScribeJava here:");
        final String authorizationUrl = service.getAuthorizationUrl(requestToken);
        System.out.println(authorizationUrl);
        System.out.println("And paste the verifier here");
        System.out.print(">>");
        final String oauthVerifier = in.nextLine();
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
        System.out.println("Got the Access Token!");
        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        request.addQuerystringParameter("method", "flickr.test.login");
        service.signRequest(accessToken, request);
        final Response response = service.execute(request);
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
    }
}
