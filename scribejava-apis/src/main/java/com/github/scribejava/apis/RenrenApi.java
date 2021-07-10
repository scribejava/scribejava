package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;

/**
 * Renren(http://www.renren.com/) OAuth 2.0 based api.
 */
public class RenrenApi extends DefaultApi20 {

    protected RenrenApi() {
    }

    private static class InstanceHolder {
        private static final RenrenApi INSTANCE = new RenrenApi();
    }

    public static RenrenApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://graph.renren.com/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://graph.renren.com/oauth/authorize";
    }

    @Override
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
    }
}
