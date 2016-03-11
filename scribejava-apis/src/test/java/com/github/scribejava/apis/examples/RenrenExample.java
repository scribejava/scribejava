package com.github.scribejava.apis.examples;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

public abstract class RenrenExample {

    private static final String NETWORK_NAME = "Renren";
    private static final String PROTECTED_RESOURCE_URL = "http://api.renren.com/restserver.do";

    public static void main(String... args) {
        // Replace these with your own api key and secret
        final String apiKey = "your api key";
        final String apiSecret = "your api secret";
        final OAuth20Service service = new ServiceBuilder()
                .apiKey(apiKey)
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
        System.out.println("(if your curious it looks like this: " + accessToken
                + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL, service);
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("method", "users.getInfo");
        parameters.put("format", "json");
        parameters.put("v", "1.0");

        final List<String> sigString = new ArrayList<>(parameters.size() + 1);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            request.addQuerystringParameter(entry.getKey(), entry.getValue());
            sigString.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
        sigString.add(String.format("%s=%s", OAuthConstants.ACCESS_TOKEN, accessToken.getAccessToken()));
        Collections.sort(sigString);
        final StringBuilder b = new StringBuilder();
        for (String param : sigString) {
            b.append(param);
        }
        b.append(apiSecret);
        System.out.println("Sig string: " + b.toString());
        request.addQuerystringParameter("sig", md5(b.toString()));
        service.signRequest(accessToken, request);
        final Response response = request.send();
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
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
