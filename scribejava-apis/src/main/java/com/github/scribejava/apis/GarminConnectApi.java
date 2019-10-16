package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class GarminConnectApi extends DefaultApi10a {

    private GarminConnectApi() {
    }

    private static class InstanceHolder {
        private static final GarminConnectApi INSTANCE = new GarminConnectApi();
    }

    public static GarminConnectApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://connectapi.garmin.com/oauth-service/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://connectapi.garmin.com/oauth-service/oauth/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://connect.garmin.com/oauthConfirm";
    }
}
