package com.github.scribejava.apis.examples;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.RenrenApi;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RenrenExample {

    private static final String NETWORK_NAME = "Renren";
    private static final String PROTECTED_RESOURCE_URL = "http://api.renren.com/restserver.do";

    private RenrenExample() {
    }

    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your own api key and secret
        final String apiKey = "your api key";
        final String apiSecret = "your api secret";
        final OAuth20Service service = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .scope("status_update publish_feed")
                .callback("http://your.doman.com/oauth/renren")
                .build(RenrenApi.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl();
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final String code = in.nextLine();
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(code);
        System.out.println("Got the Access Token!");
        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("method", "users.getInfo");
        parameters.put("format", "json");
        parameters.put("v", "1.0");

        final List<String> sigString = new ArrayList<>(parameters.size() + 1);
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            request.addQuerystringParameter(parameter.getKey(), parameter.getValue());
            sigString.add(String.format("%s=%s", parameter.getKey(), parameter.getValue()));
        }
        sigString.add(String.format("%s=%s", OAuthConstants.ACCESS_TOKEN, accessToken.getAccessToken()));
        Collections.sort(sigString);
        final StringBuilder sigBuilder = new StringBuilder();
        for (String param : sigString) {
            sigBuilder.append(param);
        }
        sigBuilder.append(apiSecret);

        final String sig = sigBuilder.toString();
        System.out.println("Sig string: " + sig);
        request.addQuerystringParameter("sig", md5(sig));
        service.signRequest(accessToken, request);
        final Response response = service.execute(request);
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");

    }

    public static String md5(String orgString) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] array = md.digest(orgString.getBytes(Charset.forName("UTF-8")));
            final Formatter builder = new Formatter();
            for (byte b : array) {
                builder.format("%02x", b);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 is unsupported?", e);
        }
    }
}
