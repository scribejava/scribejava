package com.github.scribejava.core.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Default implementation of {@link TokenExtractor} for OAuth 2.0
 */
public class OAuth2AccessTokenExtractor implements TokenExtractor<OAuth2AccessToken> {

    private static final String TOKEN_REGEX = "access_token=([^&]+)";

    protected OAuth2AccessTokenExtractor() {
    }

    private static class InstanceHolder {

        private static final OAuth2AccessTokenExtractor INSTANCE = new OAuth2AccessTokenExtractor();
    }

    public static OAuth2AccessTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuth2AccessToken extract(String response) {
        Preconditions.checkEmptyString(response,
                "Response body is incorrect. Can't extract a token from an empty string");

        final Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
        if (matcher.find()) {
            final String token = OAuthEncoder.decode(matcher.group(1));
            return new OAuth2AccessToken(token, response);
        } else {
            throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'",
                    null);
        }
    }
}
