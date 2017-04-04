package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.OAuth2SignatureType;
import com.github.scribejava.core.model.Verb;

public class ViadeoApi extends DefaultApi20 {

    protected ViadeoApi() {
    }

    private static class InstanceHolder {
        private static final ViadeoApi INSTANCE = new ViadeoApi();
    }

    public static ViadeoApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://secure.viadeo.com/oauth-provider/access_token2";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://secure.viadeo.com/oauth-provider/authorize2";
    }

    @Override
    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_URI_QUERY_PARAMETER;
    }
}
