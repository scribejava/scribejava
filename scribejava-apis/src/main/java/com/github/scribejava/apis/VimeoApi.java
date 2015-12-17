package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

public class VimeoApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "http://vimeo.com/oauth/authorize?oauth_token=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "http://vimeo.com/oauth/access_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "http://vimeo.com/oauth/request_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }
}
