package com.github.scribejava.core.extractors;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Custom implementation of {@link TokenExtractor} for OAuth 2.0
 */
public class OAuth2AccessTokenExtractor implements TokenExtractor<OAuth2AccessToken> {

    private static final Pattern ACCESS_TOKEN_REGEX_PATTERN = Pattern.compile("access_token=([^&]+)");
    private static final Pattern TOKEN_TYPE_REGEX_PATTERN = Pattern.compile("token_type=([^&]+)");
    private static final Pattern EXPIRES_IN_REGEX_PATTERN = Pattern.compile("expires_in=([^&]+)");
    private static final Pattern REFRESH_TOKEN_REGEX_PATTERN = Pattern.compile("refresh_token=([^&]+)");
    private static final Pattern SCOPE_REGEX_PATTERN = Pattern.compile("scope=([^&]+)");

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
    public OAuth2AccessToken extract(Response response) throws IOException {
        if (response.getCode() != 200) {
            throw new OAuthException("Response code is not 200 but '" + response.getCode() + '\'');
        }
        final String body = response.getBody();
        Preconditions.checkEmptyString(body,
                "Response body is incorrect. Can't extract a token from an empty string");

        final String accessToken = extractParameter(body, ACCESS_TOKEN_REGEX_PATTERN, true);
        final String tokenType = extractParameter(body, TOKEN_TYPE_REGEX_PATTERN, false);
        final String expiresInString = extractParameter(body, EXPIRES_IN_REGEX_PATTERN, false);
        Integer expiresIn;
        try {
            expiresIn = expiresInString == null ? null : Integer.valueOf(expiresInString);
        } catch (NumberFormatException nfe) {
            expiresIn = null;
        }
        final String refreshToken = extractParameter(body, REFRESH_TOKEN_REGEX_PATTERN, false);
        final String scope = extractParameter(body, SCOPE_REGEX_PATTERN, false);

        return new OAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope, body);
    }

    private static String extractParameter(String response, Pattern regexPattern, boolean required)
            throws OAuthException {

        final Matcher matcher = regexPattern.matcher(response);
        if (matcher.find()) {
            return OAuthEncoder.decode(matcher.group(1));
        } else if (required) {
            throw new OAuthException("Response body is incorrect. Can't extract a '" + regexPattern.pattern()
                    + "' from this: '" + response + "'", null);
        } else {
            return null;
        }
    }
}
