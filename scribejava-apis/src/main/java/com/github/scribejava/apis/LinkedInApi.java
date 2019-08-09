package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class LinkedInApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://api.linkedin.com/uas/oauth/authenticate";
    private static final String REQUEST_TOKEN_URL = "https://api.linkedin.com/uas/oauth/requestToken";

    private final String scopesAsString;

    protected LinkedInApi() {
        scopesAsString = null;
    }

    protected LinkedInApi(String... scopes) {
        final StringBuilder builder = new StringBuilder();
        for (String scope : scopes) {
            builder.append('+').append(scope);
        }
        scopesAsString = "?scope=" + builder.substring(1);
    }

    private static class InstanceHolder {

        private static final LinkedInApi INSTANCE = new LinkedInApi();
    }

    public static LinkedInApi instance() {
        return InstanceHolder.INSTANCE;
    }

    public static LinkedInApi instance(String... scopes) {
        return scopes == null || scopes.length == 0 ? instance() : new LinkedInApi(scopes);
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.linkedin.com/uas/oauth/accessToken";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return scopesAsString == null ? REQUEST_TOKEN_URL : REQUEST_TOKEN_URL + scopesAsString;
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }
}
