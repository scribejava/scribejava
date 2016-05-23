package com.github.scribejava.apis;

import com.github.scribejava.apis.service.GeniusOAuthServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

public class GeniusApi extends DefaultApi20 {

    protected GeniusApi() {
    }

    private static class InstanceHolder {

        private static final GeniusApi INSTANCE = new GeniusApi();
    }

    public static GeniusApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.genius.com/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.genius.com/oauth/authorize";
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new GeniusOAuthServiceImpl(this, config);
    }
}
