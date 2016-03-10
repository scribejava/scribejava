package com.github.scribejava.core.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Custom implementation of {@link TokenExtractor} for OAuth 2.0
 */
public class OAuth2AccessTokenExtractor implements TokenExtractor<OAuth2AccessToken> {

    private static final String ACCESS_TOKEN_REGEX = "access_token=([^&]+)";
    private static final String TOKEN_TYPE_REGEX = "token_type=([^&]+)";
    private static final String EXPIRES_IN_REGEX = "expires_in=([^&]+)";
    private static final String REFRESH_TOKEN_REGEX = "refresh_token=([^&]+)";
    private static final String SCOPE_REGEX = "scope=([^&]+)";

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

        final String accessToken = extractParameter(response, ACCESS_TOKEN_REGEX, true);
        final String tokenType = extractParameter(response, TOKEN_TYPE_REGEX, false);
        final String expiresInString = extractParameter(response, EXPIRES_IN_REGEX, false);
        Integer expiresIn;
        try {
            expiresIn = expiresInString == null ? null : Integer.valueOf(expiresInString);
        } catch (NumberFormatException nfe) {
            expiresIn = null;
        }
        final String refreshToken = extractParameter(response, REFRESH_TOKEN_REGEX, false);
        final String scope = extractParameter(response, SCOPE_REGEX, false);

        return new OAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope, response);
    }

    private static String extractParameter(String response, String regex, boolean required) throws OAuthException {
        final Matcher matcher = Pattern.compile(regex).matcher(response);
        if (matcher.find()) {
            return OAuthEncoder.decode(matcher.group(1));
        } else if (required) {
            throw new OAuthException("Response body is incorrect. Can't extract a '" + regex
                    + "' from this: '" + response + "'", null);
        } else {
            return null;
        }
    }
}
