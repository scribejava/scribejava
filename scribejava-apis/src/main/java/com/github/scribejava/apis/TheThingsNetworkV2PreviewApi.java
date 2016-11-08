package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class TheThingsNetworkV2PreviewApi extends DefaultApi20 {

    protected TheThingsNetworkV2PreviewApi() {
    }

    private static class InstanceHolder {
        private static final TheThingsNetworkV2PreviewApi INSTANCE = new TheThingsNetworkV2PreviewApi();
    }

    public static TheThingsNetworkV2PreviewApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://preview.account.thethingsnetwork.org/users/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://preview.account.thethingsnetwork.org/users/authorize";
    }
}
