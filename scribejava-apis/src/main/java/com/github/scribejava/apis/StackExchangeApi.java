package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Stack Exchange authentication via OAuth 2.0  (stackoverflow.com,
 * askubuntu.com, etc.).
 */
public class StackExchangeApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://stackexchange.com/oauth?client_id=%s&redirect_uri=%s";

    protected StackExchangeApi() {
    }

    private static class InstanceHolder {
        private static final StackExchangeApi INSTANCE = new StackExchangeApi();
    }

    public static StackExchangeApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://stackexchange.com/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback. StackExchange does not support OOB");
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
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenExtractor.instance();
    }
}
