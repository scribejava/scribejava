package com.github.scribejava.apis;

import com.github.scribejava.apis.service.OsChinaOAuthServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

public class OschinaApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "http://www.oschina.net/action/oauth2/authorize?client_id=%s&redirect_uri=%s";

    protected OschinaApi() {
    }

    private static class InstanceHolder {
        private static final OschinaApi INSTANCE = new OschinaApi();
    }

    public static OschinaApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://www.oschina.net/action/openapi/token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback. GitHub does not support OOB");
        final StringBuilder sb = new StringBuilder(String.format(AUTHORIZE_URL, config.getApiKey(),
                OAuthEncoder.encode(config.getCallback())));
        if (config.hasScope()) {
            sb.append('&').append(OAuthConstants.SCOPE).append('=').append(OAuthEncoder.encode(config.getScope()));
        }
        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(OAuthConstants.STATE).append('=').append(OAuthEncoder.encode(state));
        }
        final String response_type = config.getResponseType();
        if (response_type != null) {
            sb.append('&').append("response_type").append('=').append(OAuthEncoder.encode(response_type));
        }
        return sb.toString();
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenExtractor.instance();
    }
    
    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new OsChinaOAuthServiceImpl(this, config);
    }
}
