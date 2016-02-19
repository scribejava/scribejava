package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class QWeiboApi extends DefaultApi10a {

    private static final String REQUEST_TOKEN_URL = "https://open.t.qq.com/cgi-bin/request_token";
    private static final String ACCESS_TOKEN_URL = "https://open.t.qq.com/cgi-bin/access_token";
    private static final String AUTHORIZE_URL = "https://open.t.qq.com/cgi-bin/authorize?oauth_token=%s";

    protected QWeiboApi() {
    }

    private static class InstanceHolder {
        private static final QWeiboApi INSTANCE = new QWeiboApi();
    }

    public static QWeiboApi instance() {
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
