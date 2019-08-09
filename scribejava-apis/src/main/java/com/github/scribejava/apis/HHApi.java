package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

public class HHApi extends DefaultApi20 {

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
        return "https://hh.ru/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://hh.ru/oauth/authorize";
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }
}
