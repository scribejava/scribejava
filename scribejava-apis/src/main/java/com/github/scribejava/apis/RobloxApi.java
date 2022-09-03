package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class RobloxApi extends DefaultApi20 {

    private RobloxApi() {
    }

    private static class InstanceHolder {
        private static final RobloxApi INSTANCE = new RobloxApi();
    }

    public static RobloxApi instance() {
        return RobloxApi.InstanceHolder.INSTANCE;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://authorize.roblox.com";
    }

    @Override
    public String getRevokeTokenEndpoint() {
        return "https://apis.roblox.com/oauth/v1/token/revoke";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://apis.roblox.com/oauth/v1/token";
    }
}
