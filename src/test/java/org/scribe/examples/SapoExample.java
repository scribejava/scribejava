package org.scribe.examples;

import java.util.*;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;


public class SapoExample
{
    private static final String NETWORK_NAME = "SAPO";
    private static final String AUTHORIZE_URL = "https://id.sapo.pt/oauth/authorize?oauth_token=";
    private static final String PROTECTED_RESOURCE_URL = "http://services.sapo.pt/Photos";
    private static final String SCOPE = "/Photos/*";

    public static void main(String[] args) {
        OAuthService service = new ServiceBuilder()
                .provider(SapoApi.class)
                .apiKey("your_api_key")
                .apiSecret("your_api_secret")
                .callback("your_callback_url")
                .scope(SCOPE).build();
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
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        request.addHeader("Content-Type", "text/xml");
        request.addHeader("SOAPAction", "http://services.sapo.pt/definitions/Photos/UserDetails");
        request.addPayload("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Header><ESBCredentials xmlns=\"http://services.sapo.pt/definitions\"><ESBToken>"
                + accessToken.getToken() + "</ESBToken></ESBCredentials></soap:Header>"
                + "<soap:Body><UserDetails xmlns=\"http://services.sapo.pt/definitions/Photos\"><user/>"
                + "<interface>png</interface></UserDetails></soap:Body></soap:Envelope>");
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
