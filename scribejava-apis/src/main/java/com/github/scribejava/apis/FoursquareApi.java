package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class FoursquareApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "http://foursquare.com/oauth/authorize";

    protected FoursquareApi() {
    }

    private static class InstanceHolder {
        private static final FoursquareApi INSTANCE = new FoursquareApi();
    }

    public static FoursquareApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://foursquare.com/oauth/access_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "http://foursquare.com/oauth/request_token";
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return AUTHORIZATION_URL;
    }
}
