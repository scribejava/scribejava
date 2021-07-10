package com.github.scribejava.apis.openid;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;

/**
 * additionally parses OpenID id_token
 */
public class OpenIdJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    protected OpenIdJsonTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final OpenIdJsonTokenExtractor INSTANCE = new OpenIdJsonTokenExtractor();
    }

    public static OpenIdJsonTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected OpenIdOAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, JsonNode response, String rawResponse) {
        final JsonNode idToken = response.get("id_token");
        return new OpenIdOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                idToken == null ? null : idToken.asText(), rawResponse);
    }
}
