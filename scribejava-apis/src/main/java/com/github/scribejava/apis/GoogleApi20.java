package com.github.scribejava.apis;

import com.github.scribejava.apis.google.GoogleJsonTokenExtractor;
import com.github.scribejava.apis.service.GoogleOAuthServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;

public class GoogleApi20 extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "https://accounts.google.com/o/oauth2/auth?response_type=%s&client_id=%s&redirect_uri=%s&scope=%s";

    protected GoogleApi20() {
    }

    private static class InstanceHolder {
        private static final GoogleApi20 INSTANCE = new GoogleApi20();
    }

    public static GoogleApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.googleapis.com/oauth2/v4/token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        final StringBuilder sb = new StringBuilder(String.format(AUTHORIZE_URL, config.getResponseType(),
                config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope())));

        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(OAuthConstants.STATE).append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return GoogleJsonTokenExtractor.instance();
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new GoogleOAuthServiceImpl(this, config);
    }
}
