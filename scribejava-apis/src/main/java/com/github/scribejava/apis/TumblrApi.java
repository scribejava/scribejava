package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class TumblrApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://www.tumblr.com/oauth/authorize";
    private static final String REQUEST_TOKEN_RESOURCE = "https://www.tumblr.com/oauth/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "https://www.tumblr.com/oauth/access_token";

    protected TumblrApi() {
    }

    private static class InstanceHolder {
        private static final TumblrApi INSTANCE = new TumblrApi();
    }

    public static TumblrApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_RESOURCE;
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }
}
