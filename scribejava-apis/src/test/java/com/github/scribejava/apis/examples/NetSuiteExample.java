package com.github.scribejava.apis.examples;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.NetSuiteApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class NetSuiteExample {
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    // Replace these with your own account id, consumer key, consumer secret, token id, token secret and restlet domain
    private static final String ACCOUNT_ID = "your_account_id";
    private static final String CONSUMER_KEY = "your_consumer_key";
    private static final String CONSUMER_SECRET = "your_consumer_secret";
    private static final String TOKEN_ID = "your_token_id";
    private static final String TOKEN_SECRET = "your_token_secret";
    private static final String RESTLET_DOMAIN = "YOUR-ACCOUNT-ID.restlets.api.netsuite.com";

    private static final String RESTLET_PATH = "/app/site/hosting/restlet.nl";
    private static final String SCRIPT = "script";
    private static final String DEPLOY = "deploy";
    private static final String PROTECTED_RESOURCE_URL = new URIBuilder()
            .setScheme("https")
            .setHost(RESTLET_DOMAIN)
            .setPath(RESTLET_PATH)
            .addParameter(SCRIPT, "your_restlet_script_id")
            .addParameter(DEPLOY, "your_restlet_deploy_id")
            .toString();

    private NetSuiteExample() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        final OAuth10aService service = new ServiceBuilder(CONSUMER_KEY)
                .apiSecret(CONSUMER_SECRET)
                .build(NetSuiteApi.instance());

        System.out.println("=== NetSuite's OAuth (Token-based Authorization) ===");
        System.out.println();

        final OAuth1AccessToken accessToken = new OAuth1AccessToken(TOKEN_ID, TOKEN_SECRET);

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
        request.setRealm(ACCOUNT_ID);
        service.signRequest(accessToken, request);
        request.addHeader("Content-Type", "application/json");

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("message", "your request body for POST and PUT");
        String jsonRequestBody = jsonMapper.writer().writeValueAsString(requestMap);
        request.setPayload(jsonRequestBody);

        System.out.println("Request body to send:");
        System.out.println(jsonRequestBody);
        System.out.println();

        try (Response response = service.execute(request)) {
            System.out.println("Got it! Let's see what we found...");
            System.out.println();
            System.out.println(response.getCode());
            System.out.println(response.getBody());
        }

        System.out.println();
        System.out.println("That's it man! Go and build something awesome with ScribeJava! :)");
    }
}
