package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

public class XingApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://api.xing.com/v1/authorize?oauth_token=%s";

    private XingApi() {
    }

    private static class InstanceHolder {
        private static final XingApi INSTANCE = new XingApi();
    }

    public static XingApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.xing.com/v1/access_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://api.xing.com/v1/request_token";
    }

    @Override
    public String getAuthorizationUrl(final Token requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

}
