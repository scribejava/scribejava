package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

/**
 * OAuth2 api for NetBase (http://www.netbase.com/)
 */
public class NetBaseApi extends DefaultApi20 {

    public static NetBaseApi instance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static final NetBaseApi INSTANCE = new NetBaseApi();
    }

    private NetBaseApi() {}

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.netbase.com/cb/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.netbase.com/cb/oauth/authorize";
    }
}
