package com.github.scribejava.apis.examples;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.apis.KeycloakApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;

public class KeycloakExampleJWTClientAuth {

    private KeycloakExampleJWTClientAuth() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your own api key, secret, callback, base url and realm
        final String apiKey = "your_api_key";
        final String keyId = "your_public_key_id";
        final String callback = "your_callback";
        final String baseUrl = "your_base_url";
        final String realm = "your_realm";
        final RSAPrivateKey privateKey = null; // your private key

        final OAuth20Service service = new ServiceBuilder(apiKey)
                .apiSecret(apiKey)
                .defaultScope("openid")
                .callback(callback)
                .build(KeycloakApi.instance(baseUrl, realm, privateKey, keyId));

        System.out.println("=== Keyloack's OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final OAuth2AccessToken token = service.getAccessTokenClientCredentialsGrant();
        System.out.println("Access token: " + token.getAccessToken());
    }
}
