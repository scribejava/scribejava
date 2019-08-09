package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class EtsyApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://www.etsy.com/oauth/signin";
    private static final String ACCESS_TOKEN_URL = "https://openapi.etsy.com/v2/oauth/access_token";
    private static final String REQUEST_TOKEN_URL = "https://openapi.etsy.com/v2/oauth/request_token";

    private final String scopeAsString;

    private EtsyApi() {
        scopeAsString = null;
    }

    private EtsyApi(String... scopes) {
        final StringBuilder builder = new StringBuilder();
        for (String scope : scopes) {
            builder.append("%20").append(scope);
        }
        scopeAsString = "?scope=" + builder.substring(3);
    }

    private static class InstanceHolder {
        private static final EtsyApi INSTANCE = new EtsyApi();
    }

    public static EtsyApi instance() {
        return InstanceHolder.INSTANCE;
    }

    public static EtsyApi instance(String... scopes) {
        return scopes == null || scopes.length == 0 ? instance() : new EtsyApi(scopes);
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return scopeAsString == null ? REQUEST_TOKEN_URL : REQUEST_TOKEN_URL + scopeAsString;
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }
}
