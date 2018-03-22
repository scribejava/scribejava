package com.github.scribejava.apis.vk;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import java.util.regex.Pattern;

/**
 * additionally parses email
 */
public class VKJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    private static final Pattern EMAIL_REGEX_PATTERN = Pattern.compile("\"email\"\\s*:\\s*\"(\\S*?)\"");

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
            String refreshToken, String scope, String response) {
        return new VKOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                extractParameter(response, EMAIL_REGEX_PATTERN, false), response);
    }
}
