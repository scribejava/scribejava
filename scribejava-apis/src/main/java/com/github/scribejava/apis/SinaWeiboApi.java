package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class SinaWeiboApi extends DefaultApi10a {

    private static final String REQUEST_TOKEN_URL = "http://api.t.sina.com.cn/oauth/request_token";
    private static final String ACCESS_TOKEN_URL = "http://api.t.sina.com.cn/oauth/access_token";
    private static final String AUTHORIZE_URL = "http://api.t.sina.com.cn/oauth/authorize";

    protected SinaWeiboApi() {
    }

    private static class InstanceHolder {
        private static final SinaWeiboApi INSTANCE = new SinaWeiboApi();
    }

    public static SinaWeiboApi instance() {
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
    public String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }
}
