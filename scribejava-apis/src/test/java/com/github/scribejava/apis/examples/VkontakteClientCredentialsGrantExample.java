package com.github.scribejava.apis.examples;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.VkontakteApi;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class VkontakteClientCredentialsGrantExample {

    private static final String NETWORK_NAME = "Vkontakte.ru";

    private VkontakteClientCredentialsGrantExample() {
    }

    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .scope("wall,offline") // replace with desired scope
                .callback("http://your.site.com/callback")
                .build(VkontakteApi.instance());

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();
        final OAuth2AccessToken accessToken = service.getAccessTokenClientCredentialsGrant();

        System.out.println("Got the Access Token!");
        System.out.println(accessToken.getRawResponse());
        System.out.println();

        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
    }
}
