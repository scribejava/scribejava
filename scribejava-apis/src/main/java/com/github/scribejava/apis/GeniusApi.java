package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class GeniusApi extends DefaultApi20 {

    protected GeniusApi() {
    }

    private static class InstanceHolder {

        private static final GeniusApi INSTANCE = new GeniusApi();
    }

    public static GeniusApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.genius.com/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.genius.com/oauth/authorize";
    }
}
