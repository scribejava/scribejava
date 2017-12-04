package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class DataportenApi extends DefaultApi20 {

    protected DataportenApi() {
    }

    private static class InstanceHolder {
        private static final DataportenApi INSTANCE = new DataportenApi();
    }

    public static DataportenApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://auth.dataporten.no/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://auth.dataporten.no/oauth/authorization";
    }
}
