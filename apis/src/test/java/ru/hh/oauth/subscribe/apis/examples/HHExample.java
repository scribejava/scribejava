package ru.hh.oauth.subscribe.apis.examples;

import java.util.Scanner;

import ru.hh.oauth.subscribe.core.builder.ServiceBuilder;
import ru.hh.oauth.subscribe.core.model.OAuthRequest;
import ru.hh.oauth.subscribe.core.model.Response;
import ru.hh.oauth.subscribe.core.model.Token;
import ru.hh.oauth.subscribe.core.model.Verb;
import ru.hh.oauth.subscribe.core.model.Verifier;
import ru.hh.oauth.subscribe.core.oauth.OAuthService;

import ru.hh.oauth.subscribe.apis.HHApi;

public class HHExample {

    private static final String NETWORK_NAME = "hh.ru";
    private static final String PROTECTED_RESOURCE_URL = "https://api.hh.ru/me";
    private static final Token EMPTY_TOKEN = null;

    public static void main(String[] args) {
        // Replace these with your own client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";
        OAuthService service = new ServiceBuilder()
                .provider(HHApi.class)
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .callback("http://your.site.com/callback")
                .build();
        Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize SubScribe here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final Verifier verifier = new Verifier(in.nextLine());
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println();

        System.out.println("Now we're going to access a protected resource...");
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with SubScribe! :)");
    }
}
