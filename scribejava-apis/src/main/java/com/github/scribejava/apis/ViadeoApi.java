package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;

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
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
    }
}
