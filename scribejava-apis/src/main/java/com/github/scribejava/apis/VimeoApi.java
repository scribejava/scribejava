package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class VimeoApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "http://vimeo.com/oauth/authorize?oauth_token=%s";

    protected VimeoApi() {
    }

    private static class InstanceHolder {
        private static final VimeoApi INSTANCE = new VimeoApi();
    }

    public static VimeoApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://vimeo.com/oauth/access_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "http://vimeo.com/oauth/request_token";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }
}
