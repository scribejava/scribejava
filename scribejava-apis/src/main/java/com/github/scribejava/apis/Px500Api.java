package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class Px500Api extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "https://api.500px.com/v1/oauth/authorize?oauth_token=%s";

    protected Px500Api() {
    }

    private static class InstanceHolder {
        private static final Px500Api INSTANCE = new Px500Api();
    }

    public static Px500Api instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.500px.com/v1/oauth/access_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://api.500px.com/v1/oauth/request_token";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }
}
