package com.github.scribejava.apis.examples;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.XeroApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class XeroExample {

    private static final String NETWORK_NAME = "Xero";
    private static final String PROTECTED_RESOURCE_URL = "https://api.xero.com/connections";
    private static final String PROTECTED_ORGANISATION_URL = "https://api.xero.com/api.xro/2.0/Organisation";

    private XeroExample() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";
        final String callback = "your callback url";

        final String secretState = "secret" + new Random().nextInt(999_999);
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                // replace with desired scope
                .defaultScope("openid email profile offline_access accounting.settings accounting.transactions")
                .callback(callback)
                .build(XeroApi20.instance());
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

        //First GET the Xero Tenant ID
        System.out.println("Getting Xero tenant id...");
        final OAuthRequest requestConn = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        requestConn.addHeader("Accept", "application/json");
        service.signRequest(accessToken.getAccessToken(), requestConn);
        final Response connResp = service.execute(requestConn);

        final ObjectMapper objectMapper = new ObjectMapper();
        final List<Map<String, String>> tenantList = objectMapper.readValue(connResp.getBody(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));

        System.out.println();
        System.out.println(connResp.getCode());
        System.out.println(connResp.getBody());
        System.out.println();
        System.out.println("Your Xero tenant id is ...." + tenantList.get(0).get("tenantId"));
        System.out.println();

        // GET protected Resource
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_ORGANISATION_URL);
        request.addHeader("xero-tenant-id", tenantList.get(0).get("tenantId"));
        service.signRequest(accessToken.getAccessToken(), request);
        final Response response = service.execute(request);

        // Now let's go and ask for a protected resource!
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
    }
}
