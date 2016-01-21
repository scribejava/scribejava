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
    
    private static final String REGEX_GROUP = "=([^&]+)";

    private final String accessTokenKey;
    private final String refreshTokenKey;
    private final String expiresInKey;
    private final String expiresAtKey;
    private final String tokenTypeKey;

    public OAuth2AccessTokenExtractorImpl(String accessTokenKey, String refreshTokenKey, String expiresInKey, String expiresAtKey, String tokenTypeKey) {
        this.accessTokenKey = accessTokenKey;
        this.refreshTokenKey = refreshTokenKey;
        this.expiresInKey = expiresInKey;
        this.expiresAtKey = expiresAtKey;
        this.tokenTypeKey = tokenTypeKey;
    }

    public OAuth2AccessTokenExtractorImpl() {
        this("access_token","refresh_token","expires_in","expires","token_type");
    } 
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AccessToken extract(final String response) {
        Preconditions.checkEmptyString(response,"Response body is incorrect. Can't extract a token from an empty string");

        final Matcher tokenMatcher = Pattern.compile(accessTokenKey+REGEX_GROUP).matcher(response);
        final Matcher refreshMatcher = Pattern.compile(refreshTokenKey+REGEX_GROUP).matcher(response);
        final Matcher typeMatcher = Pattern.compile(tokenTypeKey+REGEX_GROUP).matcher(response);
        final Matcher expiresInMatcher = Pattern.compile(expiresInKey+REGEX_GROUP).matcher(response);
        final Matcher expiresMatcher = Pattern.compile(expiresAtKey+REGEX_GROUP).matcher(response);
        
        String refreshToken = null;
        String tokenType = null;
        Long expiresIn = null;
        
        if (refreshMatcher.find()) {
            refreshToken = OAuthEncoder.decode(refreshMatcher.group(1));
        }
        
        if (typeMatcher.find()) {
            tokenType = OAuthEncoder.decode(typeMatcher.group(1));
        }
        
        if (expiresInMatcher.find()) {
            expiresIn = Long.parseLong(OAuthEncoder.decode(expiresInMatcher.group(1)));
        } else if (expiresMatcher.find()) {
            expiresIn = Long.parseLong(OAuthEncoder.decode(expiresMatcher.group(1)))-System.currentTimeMillis();
        }
        
        if (tokenMatcher.find()) {
            final String token = OAuthEncoder.decode(tokenMatcher.group(1));
            return new OAuth2AccessToken(token, tokenType, refreshToken, expiresIn, response);
        } else {
            throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
        }
    }
}
