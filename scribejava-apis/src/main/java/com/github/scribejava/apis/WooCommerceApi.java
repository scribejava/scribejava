package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.Verb;

public class WooCommerceApi extends DefaultApi10a {

    protected WooCommerceApi() {
    }

    private static class InstanceHolder {
        private static final WooCommerceApi INSTANCE = new WooCommerceApi();
    }

    public static WooCommerceApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return null;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return null;
    }

    @Override
    public Verb getRequestTokenVerb() {
        return null;
    }

    @Override
    public boolean isEmptyOAuthTokenParamIsRequired() {
        return true;
    }

}
