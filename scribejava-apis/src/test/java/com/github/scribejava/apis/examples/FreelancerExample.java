package com.github.scribejava.apis.examples;

import java.util.Scanner;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.FreelancerApi;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class FreelancerExample {

    private static final String NETWORK_NAME = "Freelancer";
    private static final String AUTHORIZE_URL
            = "http://www.sandbox.freelancer.com/users/api-token/auth.php?oauth_token=";
    private static final String PROTECTED_RESOURCE_URL = "http://api.sandbox.freelancer.com/Job/getJobList.json";
    private static final String SCOPE = "http://api.sandbox.freelancer.com";

    private FreelancerExample() {
    }

    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        final OAuth10aService service = new ServiceBuilder("your client id")
                .apiSecret("your client secret")
                .scope(SCOPE)
                .build(FreelancerApi.Sandbox.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        final OAuth1RequestToken requestToken = service.getRequestToken();
        System.out.println("Got the Request Token!");
        System.out.println("(The raw response looks like this: " + requestToken.getRawResponse() + "')");
        System.out.println();

        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(AUTHORIZE_URL + requestToken.getToken());
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
        service.signRequest(accessToken, request);
        request.addHeader("GData-Version", "3.0");
        final Response response = service.execute(request);
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
    }
}
