package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class InstagramApi20 extends DefaultApi20 {

    protected InstagramApi20() {
    }

    private static class InstanceHolder {
        private static final InstagramApi20 INSTANCE = new InstagramApi20();
    }

    public static InstagramApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.instagram.com/oauth/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.instagram.com/oauth/authorize";
    }
}
