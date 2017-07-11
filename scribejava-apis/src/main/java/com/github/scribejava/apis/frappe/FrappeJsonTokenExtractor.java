package com.github.scribejava.apis.frappe;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import java.util.regex.Pattern;

/**
 * additionally parses OpenID id_token
 */
public class FrappeJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    private static final Pattern ID_TOKEN_REGEX_PATTERN = Pattern.compile("\"id_token\"\\s*:\\s*\"(\\S*?)\"");

    protected FrappeJsonTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final FrappeJsonTokenExtractor INSTANCE = new FrappeJsonTokenExtractor();
    }

    public static FrappeJsonTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected FrappeToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, String response) {
        return new FrappeToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                extractParameter(response, ID_TOKEN_REGEX_PATTERN, false), response);
    }
}
