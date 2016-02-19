package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class NetProspexApi extends DefaultApi10a {

    private static final String REQUEST_TOKEN_URL = "https://api.netprospex.com/1.0/oauth/request-token";
    private static final String ACCESS_TOKEN_URL = "https://api.netprospex.com/1.0/oauth/access-token";
    private static final String AUTHORIZE_URL = "https://api.netprospex.com/1.0/oauth/authorize?oauth_token=%s";

    protected NetProspexApi() {
    }

    private static class InstanceHolder {
        private static final NetProspexApi INSTANCE = new NetProspexApi();
    }

    public static NetProspexApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_URL;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}
