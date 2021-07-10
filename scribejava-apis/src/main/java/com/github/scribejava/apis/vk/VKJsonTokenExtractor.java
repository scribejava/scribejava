package com.github.scribejava.apis.vk;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;

/**
 * additionally parses email
 */
public class VKJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    protected VKJsonTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final VKJsonTokenExtractor INSTANCE = new VKJsonTokenExtractor();
    }

    public static VKJsonTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected VKOAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, JsonNode response, String rawResponse) {
        final JsonNode email = response.get("email");
        return new VKOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                email == null ? null : email.asText(), rawResponse);
    }
}
