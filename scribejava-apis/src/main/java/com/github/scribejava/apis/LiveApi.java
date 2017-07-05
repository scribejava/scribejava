package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.OAuth2SignatureType;

public class LiveApi extends DefaultApi20 {

    protected LiveApi() {
    }

    private static class InstanceHolder {
        private static final LiveApi INSTANCE = new LiveApi();
    }

    public static LiveApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://login.live.com/oauth20_token.srf";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://login.live.com/oauth20_authorize.srf";
    }

    @Override
    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_URI_QUERY_PARAMETER;
    }
}
