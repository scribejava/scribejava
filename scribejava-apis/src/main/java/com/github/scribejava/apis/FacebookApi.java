package com.github.scribejava.apis;

import com.github.scribejava.apis.service.FacebookOAuth20ServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.AccessTokenExtractor;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractorImpl;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * *
 * Facebook v2.5 API
 *
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
    public String getAccessTokenEndpoint() {
        return "https://graph.facebook.com/v2.5/oauth/access_token";
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new OAuth2AccessTokenExtractorImpl("access_token", "refresh_token", "expires", null, "token_type");
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

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new FacebookOAuth20ServiceImpl(this, config);
    }

}
