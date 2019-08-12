package com.github.scribejava.apis.examples;

import java.util.Random;
import java.util.Scanner;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/*
 * This full example needs a library to parse
 * the JSON response and obtain a Xero Tenant Id
 * in order to access protected resources.
 * 
 * If you add the following dependency, you can then
 * uncomment the rest of the example code to access
 * the Xero Organisation and other protected resources
 * 
 * <dependency>
        <groupId>com.googlecode.json-simple</groupId>
        <artifactId>json-simple</artifactId>
        <version>1.1.1</version>
    </dependency>
 * 
 */

/*
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
*/

public class XeroExample {

    private static final String NETWORK_NAME = "Xero";
    private static final String PROTECTED_RESOURCE_URL = "https://api.xero.com/connections";
    private static final String PROTECTED_ORGANISATION_URL = "https://api.xero.com/api.xro/2.0/Organisation";

    private XeroExample() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your client id, secret and redirect uri
        final String clientId = "your client id";
        final String clientSecret = "your client secret";
        final String callback = "your redirect uri";        
        final String secretState = "secret" + new Random().nextInt(999_999);
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .defaultScope("openid email profile offline_access accounting.settings accounting.transactions") // replace with desired scope
                .callback(callback)
                .build(Xero20Api.instance());
        final Scanner in = new Scanner(System.in, "UTF-8");

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl(secretState);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final String code = in.nextLine();
        System.out.println();

        System.out.println("And paste the state from server here. We have set 'secretState'='" + secretState + "'.");
        System.out.print(">>");
        final String value = in.nextLine();
        if (secretState.equals(value)) {
            System.out.println("State value does match!");
        } else {
            System.out.println("Ooops, state value does not match!");
            System.out.println("Expected = " + secretState);
            System.out.println("Got      = " + value);
            System.out.println();
        }

        System.out.println("Trading the Authorization Code for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(code);
        System.out.println("Got the Access Token!");
        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
        System.out.println();
        
        // GET the Xero Tenant ID
        System.out.println("Getting Xero tenant id...");
        final OAuthRequest requestConn = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        requestConn.addHeader("Accept", "application/json");
        service.signRequest(accessToken.getAccessToken(), requestConn);
        final Response responseConn = service.execute(requestConn);
        System.out.println();
        System.out.println(responseConn.getCode());
        System.out.println(responseConn.getBody());
        /*
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) parser.parse(responseConn.getBody());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        System.out.println("Your Xero tenant id is ...." + jsonObject.get("tenantId"));
        
        // GET protected Resource
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_ORGANISATION_URL);
        request.addHeader("xero-tenant-id",jsonObject.get("tenantId").toString());
        service.signRequest(accessToken.getAccessToken(), request);
        final Response response = service.execute(request);
        
        // Now let's go and ask for a protected resource!
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        */
        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
    }
}