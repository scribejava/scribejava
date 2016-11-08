package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class TheThingsNetworkV1StagingApi extends DefaultApi20 {

    protected TheThingsNetworkV1StagingApi() {
    }

    private static class InstanceHolder {
        private static final TheThingsNetworkV1StagingApi INSTANCE = new TheThingsNetworkV1StagingApi();
    }

    public static TheThingsNetworkV1StagingApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://account.thethingsnetwork.org/users/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://account.thethingsnetwork.org/users/authorize";
    }
}
