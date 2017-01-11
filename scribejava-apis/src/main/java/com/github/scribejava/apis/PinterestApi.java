package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.SignatureType;

public class PinterestApi extends DefaultApi20 {

    protected PinterestApi() {
    }

    private static class InstanceHolder {
        private static final PinterestApi INSTANCE = new PinterestApi();
    }

    public static PinterestApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.pinterest.com/v1/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.pinterest.com/oauth";
    }

    @Override
    public SignatureType getSignatureType() {
        return SignatureType.BEARER_URI_QUERY_PARAMETER;
    }
}
