package com.github.scribejava.core.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Default implementation of {@link AccessTokenExtractor}. Conforms to OAuth 2.0
 *
 */
public class OAuth2AccessTokenExtractorImpl implements AccessTokenExtractor {

    private static final String TOKEN_REGEX = "access_token=([^&]+)";
    private static final String REFRESH_REGEX = "refresh_token=([^&]+)";
    private static final String TYPE_REGEX = "token_type=([^&]+)";
    private static final String EXPIRES_REGEX = "expires_in=([^&]+)";

    /**
     * {@inheritDoc}
     */
    @Override
    public AccessToken extract(final String response) {
        Preconditions.checkEmptyString(response,"Response body is incorrect. Can't extract a token from an empty string");

        final Matcher tokenMatcher = Pattern.compile(TOKEN_REGEX).matcher(response);
        final Matcher refreshMatcher = Pattern.compile(REFRESH_REGEX).matcher(response);
        final Matcher typeMatcher = Pattern.compile(TYPE_REGEX).matcher(response);
        final Matcher expiresMatcher = Pattern.compile(EXPIRES_REGEX).matcher(response);
        
        String refreshToken = null;
        String tokenType = null;
        Long expiresIn = null;
        
        if (refreshMatcher.find()) {
            refreshToken = OAuthEncoder.decode(refreshMatcher.group(1));
        }
        
        if (typeMatcher.find()) {
            tokenType = OAuthEncoder.decode(typeMatcher.group(1));
        }
        
        if (expiresMatcher.find()) {
            expiresIn = Long.parseLong(OAuthEncoder.decode(expiresMatcher.group(1)));
        }
        
        if (tokenMatcher.find()) {
            final String token = OAuthEncoder.decode(tokenMatcher.group(1));
            return new OAuth2AccessToken(token, tokenType, refreshToken, expiresIn, response);
        } else {
            throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
        }
    }
}
