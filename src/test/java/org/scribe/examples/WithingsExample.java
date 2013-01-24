package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.WithingsApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.util.AbstractMap;
import java.util.Scanner;

/**
 * @author Candide Kemmler (candide@fluxtream.com)
 */
public class WithingsExample {
    private static final String PROTECTED_RESOURCE_URL = "http://wbsapi.withings.net/measure?action=getmeas";

    public static void main(String[] args)
    {
        // Replace these with your own api key and secret
        final String apiKey = "your api key";
        final String apiSecret = "your api secret";
        final String publickey = "your public key";
        OAuthService service = new ServiceBuilder().provider(WithingsApi.class).apiKey(apiKey).apiSecret(apiSecret).build();
        Scanner in = new Scanner(System.in);

        System.out.println("=== Withings's OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        Token requestToken = service.getRequestToken();
        System.out.println("Got the Request Token!");
        System.out.println();

        System.out.println("Now go and authorize Scribe here:");
        String authorizationUrl = service.getAuthorizationUrl(requestToken);
        System.out.println(authorizationUrl);
        System.out.println("And paste the verifier here");
        System.out.print(">>");
        Verifier verifier = new Verifier(in.nextLine());
        System.out.println("Now the userid");
        System.out.print(">>");
        String userid = in.nextLine();
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        AbstractMap.SimpleEntry<String,String> useridParameter = new AbstractMap.SimpleEntry<String, String>("userid", userid);
        Token accessToken = service.getAccessToken(requestToken, verifier, useridParameter);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        request.addQuerystringParameter("userid", userid);
        request.addQuerystringParameter("startdate", "0");
        request.addQuerystringParameter("publickey", publickey);
        request.addQuerystringParameter("enddate", String.valueOf(System.currentTimeMillis()/1000));
        service.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
    }
}
