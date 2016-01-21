package com.github.scribejava.apis.google;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.scribejava.core.extractors.OAuth2JsonAccessTokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;

/**
 * additionally parses OpenID id_token
 */
public class GoogleJsonTokenExtractor extends OAuth2JsonAccessTokenExtractor {

    private static final Pattern ID_TOKEN_PATTERN = Pattern.compile("\"id_token\"\\s*:\\s*\"(\\S*?)\"");

    public GoogleJsonTokenExtractor() {
        super("access_token","refresh_token",null,"expiry_date","token_type");
    }
    
    @Override
    public GoogleToken extract(final String response) {
        OAuth2AccessToken token = (OAuth2AccessToken)super.extract(response);
        
        return new GoogleToken(token.getToken(), token.getTokenType(), token.getRefreshToken(), token.getExpiresIn(), response, extractOpenIdToken(response));
    }

    private String extractOpenIdToken(final String response) {
        final Matcher matcher = ID_TOKEN_PATTERN.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
