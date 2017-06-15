package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.apis.service.MailruOAuthServiceImpl;
import com.github.scribejava.core.oauth.OAuth20Service;

public class MailruApi extends DefaultApi20 {

    protected MailruApi() {
    }

    private static class InstanceHolder {
        private static final MailruApi INSTANCE = new MailruApi();
    }

    public static MailruApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://connect.mail.ru/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://connect.mail.ru/oauth/authorize";
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new MailruOAuthServiceImpl(this, config);
    }
}
