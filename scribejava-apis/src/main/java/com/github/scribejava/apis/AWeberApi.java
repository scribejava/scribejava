package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class AWeberApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://auth.aweber.com/1.0/oauth/authorize";
    private static final String REQUEST_TOKEN_ENDPOINT = "https://auth.aweber.com/1.0/oauth/request_token";
    private static final String ACCESS_TOKEN_ENDPOINT = "https://auth.aweber.com/1.0/oauth/access_token";

    protected AWeberApi() {
    }

    private static class InstanceHolder {
        private static final AWeberApi INSTANCE = new AWeberApi();
    }

    public static AWeberApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_ENDPOINT;
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }
}
