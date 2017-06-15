package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth1Token;

public class NeteaseWeibooApi extends DefaultApi10a {

    private static final String REQUEST_TOKEN_URL = "http://api.t.163.com/oauth/request_token";
    private static final String ACCESS_TOKEN_URL = "http://api.t.163.com/oauth/access_token";
    private static final String AUTHORIZE_URL = "http://api.t.163.com/oauth/authorize?oauth_token=%s";
    private static final String AUTHENTICATE_URL = "http://api.t.163.com/oauth/authenticate?oauth_token=%s";

    protected NeteaseWeibooApi() {
    }

    private static class InstanceHolder {
        private static final NeteaseWeibooApi INSTANCE = new NeteaseWeibooApi();
    }

    public static NeteaseWeibooApi instance() {
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

    /**
     * this method will ignore your callback if you're creating a desktop client please choose this url else your can
     * call getAuthenticateUrl
     *
     * via
     * http://open.t.163.com/wiki/index.php?title=%E8%AF%B7%E6%B1%82%E7%94%A8%E6%88%B7%E6%8E%88%E6%9D%83Token(oauth/authorize)
     * @return url to redirect user to (to get code)
     */
    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

    /**
     * this method is for web client with callback url if you're creating a desktop client please call
     * getAuthorizationUrl
     *
     * via
     * http://open.t.163.com/wiki/index.php?title=%E8%AF%B7%E6%B1%82%E7%94%A8%E6%88%B7%E6%8E%88%E6%9D%83Token(oauth/authenticate)
     *
     * @param requestToken Token
     * @return String
     */
    public String getAuthenticateUrl(OAuth1Token requestToken) {
        return String.format(AUTHENTICATE_URL, requestToken.getToken());
    }
}
