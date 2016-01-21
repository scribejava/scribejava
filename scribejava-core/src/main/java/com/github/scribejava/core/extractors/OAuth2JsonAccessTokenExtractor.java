package com.github.scribejava.core.extractors;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.utils.Preconditions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Daniel Tyreus
 */
public class OAuth2JsonAccessTokenExtractor implements AccessTokenExtractor {
    
    private final String accessTokenKey;
    private final String refreshTokenKey;
    private final String expiresInKey;
    private final String expiresAtKey;
    private final String tokenTypeKey;

    public OAuth2JsonAccessTokenExtractor(String accessTokenKey, String refreshTokenKey, String expiresInKey, String expiresAtKey, String tokenTypeKey) {
        this.accessTokenKey = accessTokenKey;
        this.refreshTokenKey = refreshTokenKey;
        this.expiresInKey = expiresInKey;
        this.tokenTypeKey = tokenTypeKey;
        this.expiresAtKey = expiresAtKey;
    }

    public OAuth2JsonAccessTokenExtractor() {
        this("access_token","refresh_token","expires_in","expires","token_type");
    }
    

    @Override
    public AccessToken extract(final String response) {
        
        Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
        
        final Matcher accessTokenMatcher = Pattern.compile("\""+accessTokenKey+"\"\\s*:\\s*\"(\\S*?)\"").matcher(response);
        final Matcher refreshTokenMatcher = Pattern.compile("\""+refreshTokenKey+"\"\\s*:\\s*\"(\\S*?)\"").matcher(response);
        final Matcher expiresInMatcher = Pattern.compile("\""+expiresInKey+"\"\\s*:\\s*(\\d*?)\\D").matcher(response);
        final Matcher expiresAtMatcher = Pattern.compile("\""+expiresAtKey+"\"\\s*:\\s*(\\d*?)\\D").matcher(response);
        final Matcher tokenTypeMatcher = Pattern.compile("\""+tokenTypeKey+"\"\\s*:\\s*\"(\\S*?)\"").matcher(response);
        
        String refreshToken = null;
        String tokenType = null;
        Long expiresIn = null;
        Long expiresAt = null;
        
        if (refreshTokenMatcher.find()) {
            refreshToken = refreshTokenMatcher.group(1);
        }
        
        if (tokenTypeMatcher.find()) {
            tokenType = tokenTypeMatcher.group(1);
        }
        
        //first check for expires_in since that is the spec. if no expires_in found, check expires_at
        if (expiresInMatcher.find()) {
            expiresIn = Long.parseLong(expiresInMatcher.group(1));
        } else if (expiresAtMatcher.find()) {
            expiresAt = Long.parseLong(expiresAtMatcher.group(1));
            expiresIn = expiresAt - System.currentTimeMillis();
        }
        
        if (accessTokenMatcher.find()) {
            String accessToken = accessTokenMatcher.group(1);
            return new OAuth2AccessToken(accessToken,tokenType,refreshToken,expiresIn,response);
        } else {
            throw new OAuthException("Cannot extract an access token. Response was: " + response);
        }
    }

}
