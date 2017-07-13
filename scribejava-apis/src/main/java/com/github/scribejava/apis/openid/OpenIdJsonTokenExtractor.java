package com.github.scribejava.apis.openid;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import java.util.regex.Pattern;

/**
 * additionally parses OpenID id_token
 */
public class OpenIdJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    private static final Pattern ID_TOKEN_REGEX_PATTERN = Pattern.compile("\"id_token\"\\s*:\\s*\"(\\S*?)\"");

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
            String refreshToken, String scope, String response) {
        return new OpenIdOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                extractParameter(response, ID_TOKEN_REGEX_PATTERN, false), response);
    }
}
