package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.apis.service.TutByOAuthServiceImpl;
import com.github.scribejava.core.oauth.OAuth20Service;

public class TutByApi extends DefaultApi20 {

    protected TutByApi() {
    }

    private static class InstanceHolder {
        private static final TutByApi INSTANCE = new TutByApi();
    }

    public static TutByApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://profile.tut.by/getToken";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "http://profile.tut.by/auth";
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new TutByOAuthServiceImpl(this, config);
    }
}
