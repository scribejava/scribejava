package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class YahooApi20 extends DefaultApi20 {

    protected YahooApi20() {
    }

    private static class InstanceHolder {
        private static final YahooApi20 INSTANCE = new YahooApi20();
    }

    public static YahooApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.login.yahoo.com/oauth2/get_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.login.yahoo.com/oauth2/request_auth";
    }
}
