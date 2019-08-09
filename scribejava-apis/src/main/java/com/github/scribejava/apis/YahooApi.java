package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class YahooApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://api.login.yahoo.com/oauth/v2/request_auth";

    protected YahooApi() {
    }

    private static class InstanceHolder {
        private static final YahooApi INSTANCE = new YahooApi();
    }

    public static YahooApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.login.yahoo.com/oauth/v2/get_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://api.login.yahoo.com/oauth/v2/get_request_token";
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }
}
