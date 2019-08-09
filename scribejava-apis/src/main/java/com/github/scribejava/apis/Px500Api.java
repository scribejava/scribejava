package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class Px500Api extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "https://api.500px.com/v1/oauth/authorize";

    protected Px500Api() {
    }

    private static class InstanceHolder {
        private static final Px500Api INSTANCE = new Px500Api();
    }

    public static Px500Api instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.500px.com/v1/oauth/access_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://api.500px.com/v1/oauth/request_token";
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return AUTHORIZATION_URL;
    }
}
