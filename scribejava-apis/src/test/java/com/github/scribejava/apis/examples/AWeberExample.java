package com.github.scribejava.apis.examples;

import java.util.Scanner;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.AWeberApi;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AWeberExample {

    //To get your consumer key/secret, and view API docs, see https://labs.aweber.com/docs
    private static final String ACCOUNT_RESOURCE_URL = "https://api.aweber.com/1.0/accounts/";

    private static final String CONSUMER_KEY = "";
    private static final String CONSUMER_SECRET = "";

    private AWeberExample() {
    }

    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        final OAuth10aService service = new ServiceBuilder(CONSUMER_KEY)
                .apiSecret(CONSUMER_SECRET)
                .build(AWeberApi.instance());

        final Scanner in = new Scanner(System.in);

        System.out.println("=== AWeber's OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        final OAuth1RequestToken requestToken = service.getRequestToken();
        System.out.println("Got the Request Token!");
        System.out.println();

        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(service.getAuthorizationUrl(requestToken));
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
        final OAuthRequest request = new OAuthRequest(Verb.GET, ACCOUNT_RESOURCE_URL);
        service.signRequest(accessToken, request);
        final Response response = service.execute(request);
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with AWeber and ScribeJava! :)");
    }

}
