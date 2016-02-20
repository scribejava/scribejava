package com.github.scribejava.core.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.utils.Preconditions;

public class OAuth2AccessTokenJsonExtractor implements TokenExtractor<OAuth2AccessToken> {

    private static final String ACCESS_TOKENS_REGEXP = "\"access_token\"\\s*:\\s*\"(\\S*?)\"";

    protected OAuth2AccessTokenJsonExtractor() {
    }

    private static class InstanceHolder {

        private static final OAuth2AccessTokenJsonExtractor INSTANCE = new OAuth2AccessTokenJsonExtractor();
    }

    public static OAuth2AccessTokenJsonExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public OAuth2AccessToken extract(String response) {
        return new OAuth2AccessToken(extractAccessToken(response), response);
    }

    protected String extractAccessToken(String response) {
        Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
        final Matcher matcher = Pattern.compile(ACCESS_TOKENS_REGEXP).matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new OAuthException("Cannot extract an access token. Response was: " + response);
        }
    }
}
