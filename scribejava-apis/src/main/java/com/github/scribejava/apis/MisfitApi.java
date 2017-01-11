package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.SignatureType;

public class MisfitApi extends DefaultApi20 {

    protected MisfitApi() {
    }

    private static class InstanceHolder {

        private static final MisfitApi INSTANCE = new MisfitApi();
    }

    public static MisfitApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.misfitwearables.com/auth/tokens/exchange";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.misfitwearables.com/auth/dialog/authorize";
    }

    @Override
    public SignatureType getSignatureType() {
        return SignatureType.BEARER_URI_QUERY_PARAMETER;
    }
}
