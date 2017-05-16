package org.scribe.examples;

import java.util.*;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class PaypalExample {

    private static final String NETWORK_NAME = "Paypal";
    private static final String AUTHORIZE_URL =
            "https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize?client_id=%s&response_type=code&redirect_uri=%s";
    private static final String PROTECTED_RESOURCE_URL_PAYPAL_ME = "https://api.paypal.com/v1/identity/openidconnect/userinfo?schema=openid";
    private static final String SCOPE = "profile email address phone https://uri.paypal.com/services/paypalattributes";

    public static void main(String[] args) {

        String apiKey = "your_app_id";
        String apiSecret = "your_api_secret";

        OAuthService service = new ServiceBuilder()
                .provider(Paypal2Api1.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .scope(SCOPE)
                .build();
        Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        Token requestToken = service.getRequestToken();
        System.out.println("Got the Request Token!");
        System.out.println("(if your curious it looks like this: " + requestToken + " )");
        System.out.println();

        System.out.println("Now go and authorize Scribe here:");
        System.out.println(AUTHORIZE_URL + requestToken.getToken());
        System.out.println("And paste the verifier here");
        System.out.print(">>");
        Verifier verifier = new Verifier(in.nextLine());
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        Token accessToken = service.getAccessToken(requestToken, verifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL_PAYPAL_ME);
        service.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with Scribe! :)");

    }
}