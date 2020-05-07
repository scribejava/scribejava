package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.SlackApi;
import com.github.scribejava.apis.slack.SlackOAuth2AccessToken;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class SlackExample {

    private static final String NETWORK_NAME = "Slack.com";
    private static final String BOT_RESOURCE_URL = "https://slack.com/api/channels.list";
    private static final String BOT_SCOPE = "channels:read"
    private static final String USER_RESOURCE_URL = "https://slack.com/api/users.list";
    private static final String USER_SCOPE = "users:read";
    private static final String PAYLOAD = "null";
    private static final String CONTENT_TYPE_NAME = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";

    private SlackExample() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your client id and secret
        final String clientId = "client-id";
        final String clientSecret = "client-secret";
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .callback("https://www.example.com/oauth_callback/")
                .defaultScope(BOT_SCOPE)
                .build(SlackApi.instance());

        final Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");

        final Map<String, String> additionalParams = new HashMap<>();
        // define user scope if any
        additionalParams.put("user_scope", USER_SCOPE);
        final String authorizationUrl = service.createAuthorizationUrlBuilder()
                .additionalParams(additionalParams)
                .build();
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

        System.out.println("Getting info using BOT token...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, BOT_RESOURCE_URL);
        request.addHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        request.setPayload(PAYLOAD);
        service.signRequest(accessToken, request);

        try (Response response = service.execute(request)) {
            System.out.println("Got it! Lets see what we found...");
            System.out.println();
            System.out.println(response.getCode());
            System.out.println(response.getBody());
            System.out.println();
        }

        System.out.println("Getting info using USER token...");
        final OAuthRequest userRequest = new OAuthRequest(Verb.GET, USER_RESOURCE_URL);
        userRequest.addHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        userRequest.setPayload(PAYLOAD);
        SlackOAuth2AccessToken token = (SlackOAuth2AccessToken)accessToken;
        service.signRequest(token.getUserAccessToken(), userRequest);

        try (Response response = service.execute(userRequest)) {
            System.out.println("Got it! Lets see what we found...");
            System.out.println();
            System.out.println(response.getCode());
            System.out.println(response.getBody());
        }


        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
    }
}
