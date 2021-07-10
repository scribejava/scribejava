package com.github.scribejava.apis.examples;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.model.DeviceAuthorization;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Google20DeviceAuthorizationGrantExample {

    private static final String NETWORK_NAME = "Google";
    private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    private Google20DeviceAuthorizationGrantExample() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your_client_secret";

        final OAuth20Service service = new ServiceBuilder(clientId)
                .debug()
                .apiSecret(clientSecret)
                .defaultScope("profile") // replace with desired scope
                .build(GoogleApi20.instance());
        final Scanner in = new Scanner(System.in, "UTF-8");

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        System.out.println("Requesting a set of verification codes...");

        final DeviceAuthorization deviceAuthorization = service.getDeviceAuthorizationCodes();
        System.out.println("Got the Device Authorization Codes!");
        System.out.println(deviceAuthorization);

        System.out.println("Now go and authorize ScribeJava. Visit: " + deviceAuthorization.getVerificationUri()
                + " and enter the code: " + deviceAuthorization.getUserCode());
        if (deviceAuthorization.getVerificationUriComplete() != null) {
            System.out.println("Or visit " + deviceAuthorization.getVerificationUriComplete());
        }

        System.out.println("Polling for an Access Token...");
        final OAuth2AccessToken accessToken = service.pollAccessTokenDeviceAuthorizationGrant(deviceAuthorization);

        System.out.println("Got the Access Token!");
        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        while (true) {
            System.out.println("Paste fieldnames to fetch (leave empty to get profile, 'exit' to stop the example)");
            System.out.print(">>");
            final String query = in.nextLine();
            System.out.println();
            final String requestUrl;
            if ("exit".equals(query)) {
                break;
            } else if (query == null || query.isEmpty()) {
                requestUrl = PROTECTED_RESOURCE_URL;
            } else {
                requestUrl = PROTECTED_RESOURCE_URL + "?fields=" + query;
            }
            final OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
            service.signRequest(accessToken, request);
            System.out.println();
            try (Response response = service.execute(request)) {
                System.out.println(response.getCode());
                System.out.println(response.getBody());
            }
            System.out.println();
        }
    }
}
