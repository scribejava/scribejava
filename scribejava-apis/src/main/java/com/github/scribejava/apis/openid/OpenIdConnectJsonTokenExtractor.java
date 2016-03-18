package com.github.scribejava.apis.openid;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;

/**
 * additionally parses OpenID id_token
 */
public class OpenIdConnectJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {
    private static final String ID_TOKEN_REGEX = "\"id_token\"\\s*:\\s*\"(\\S*?)\"";

    protected OpenIdConnectJsonTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final OpenIdConnectJsonTokenExtractor INSTANCE = new OpenIdConnectJsonTokenExtractor();
    }

    public static OpenIdConnectJsonTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected OAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, String response) {
        return new OpenIdToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                extractParameter(response, ID_TOKEN_REGEX, false), response);
    }
}
