package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;

import com.github.scribejava.apis.service.HHOAuthServiceImpl;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;

public class HHApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://hh.ru/oauth/authorize?response_type=code&" +
        "client_id=%s&redirect_uri=%s";

    private static final String TOKEN_URL = "https://hh.ru/oauth/token";

    protected HHApi() {
    }

    private static class InstanceHolder {
        private static final HHApi INSTANCE = new HHApi();
    }

    public static HHApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new HHOAuthServiceImpl(this, config);
    }
}
