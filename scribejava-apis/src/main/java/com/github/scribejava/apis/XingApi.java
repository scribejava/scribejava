package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class XingApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://api.xing.com/v1/authorize";

    protected XingApi() {
    }

    private static class InstanceHolder {
        private static final XingApi INSTANCE = new XingApi();
    }

    public static XingApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.xing.com/v1/access_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://api.xing.com/v1/request_token";
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }

}
