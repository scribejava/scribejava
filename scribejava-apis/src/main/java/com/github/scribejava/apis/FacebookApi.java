package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Facebook v2.5 API
 */
public class FacebookApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "https://www.facebook.com/v2.5/dialog/oauth?client_id=%s&redirect_uri=%s";

    protected FacebookApi() {
    }

    private static class InstanceHolder {

        private static final FacebookApi INSTANCE = new FacebookApi();
    }

    public static FacebookApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://graph.facebook.com/v2.5/oauth/access_token";
    }

    @Override
    public String getRefreshTokenEndpoint() {
        throw new UnsupportedOperationException("Facebook doesn't support refershing tokens");
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback. Facebook does not support OOB");
        final StringBuilder sb = new StringBuilder(String.format(AUTHORIZE_URL, config.getApiKey(),
                OAuthEncoder.encode(config.getCallback())));
        if (config.hasScope()) {
            sb.append('&').append(OAuthConstants.SCOPE).append('=').append(OAuthEncoder.encode(config.getScope()));
        }

        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(OAuthConstants.STATE).append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }
}
