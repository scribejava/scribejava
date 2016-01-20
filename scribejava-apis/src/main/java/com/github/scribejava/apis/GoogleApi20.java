package com.github.scribejava.apis;

import com.github.scribejava.apis.google.GoogleJsonTokenExtractor;
import com.github.scribejava.apis.service.GoogleOAuthServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.AccessTokenExtractor;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;

public class GoogleApi20 extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=%s&redirect_uri=%s&scope=%s";

    private GoogleApi20() {
    }

    private static class InstanceHolder {
        private static final GoogleApi20 INSTANCE = new GoogleApi20();
    }

    public static GoogleApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://accounts.google.com/o/oauth2/token";
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        final StringBuilder sb = new StringBuilder(String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(
                config.getCallback()), OAuthEncoder.encode(config.getScope())));

        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(OAuthConstants.STATE).append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new GoogleJsonTokenExtractor();
    }

    @Override
    public OAuth20Service createService(final OAuthConfig config) {
        return new GoogleOAuthServiceImpl(this, config);
    }
}
