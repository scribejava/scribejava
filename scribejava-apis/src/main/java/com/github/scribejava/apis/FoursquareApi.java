package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class FoursquareApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "http://foursquare.com/oauth/authorize?oauth_token=%s";

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
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }
}
