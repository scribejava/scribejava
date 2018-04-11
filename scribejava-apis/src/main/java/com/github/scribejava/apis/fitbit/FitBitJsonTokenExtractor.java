package com.github.scribejava.apis.fitbit;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import java.util.regex.Pattern;

public class FitBitJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    private static final Pattern USER_ID_REGEX_PATTERN = Pattern.compile("\"user_id\"\\s*:\\s*\"(\\S*?)\"");

    protected FitBitJsonTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final FitBitJsonTokenExtractor INSTANCE = new FitBitJsonTokenExtractor();
    }

    public static FitBitJsonTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected FitBitOAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
            String refreshToken, String scope, String response) {
        return new FitBitOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                extractParameter(response, USER_ID_REGEX_PATTERN, false), response);
    }
}
