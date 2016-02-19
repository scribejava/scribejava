package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class MisoApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "http://gomiso.com/oauth/authorize?oauth_token=%s";
    private static final String REQUEST_TOKEN_RESOURCE = "http://gomiso.com/oauth/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "http://gomiso.com/oauth/access_token";

    protected MisoApi() {
    }

    private static class InstanceHolder {
        private static final MisoApi INSTANCE = new MisoApi();
    }

    public static MisoApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_RESOURCE;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

}
