package com.github.scribejava.apis.examples;

import java.util.Scanner;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class LinkedIn20Example {

    private static final String NETWORK_NAME = "LinkedIn";
    private static final String PROTECTED_RESOURCE_URL = "https://api.linkedin.com/v2/me";
    private static final String PROTECTED_EMAIL_RESOURCE_URL
            = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";

    private LinkedIn20Example() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .defaultScope("r_liteprofile r_emailaddress") // replace with desired scope
                .callback("http://example.com/callback")
                .build(LinkedInApi20.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String secretState = "secret" + new Random().nextInt(999_999);
        final String authorizationUrl = service.getAuthorizationUrl(secretState);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final String code = in.nextLine();
        System.out.println();

        System.out.println("Trading the Authorization Code for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(code);
        System.out.println("Got the Access Token!");
        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
        System.out.println();

        System.out.println("Now we're going to get the email of the current user...");
        final OAuthRequest emailRequest = new OAuthRequest(Verb.GET, PROTECTED_EMAIL_RESOURCE_URL);
        emailRequest.addHeader("x-li-format", "json");
        emailRequest.addHeader("Accept-Language", "ru-RU");
        service.signRequest(accessToken, emailRequest);
        System.out.println();
        try (Response emailResponse = service.execute(emailRequest)) {
            System.out.println(emailResponse.getCode());
            System.out.println(emailResponse.getBody());
        }
        System.out.println();

        System.out.println("Now we're going to access a protected profile resource...");

        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        request.addHeader("x-li-format", "json");
        request.addHeader("Accept-Language", "ru-RU");
        service.signRequest(accessToken, request);
        System.out.println();
        try (Response response = service.execute(request)) {
            System.out.println(response.getCode());
            System.out.println(response.getBody());
        }

        System.out.println();
    }
}
