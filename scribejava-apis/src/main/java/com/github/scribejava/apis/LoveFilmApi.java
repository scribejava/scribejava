package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

public class LoveFilmApi extends DefaultApi10a {

    private static final String REQUEST_TOKEN_URL = "http://openapi.lovefilm.com/oauth/request_token";
    private static final String ACCESS_TOKEN_URL = "http://openapi.lovefilm.com/oauth/access_token";
    private static final String AUTHORIZE_URL = "https://www.lovefilm.com/activate?oauth_token=%s";

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_URL;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}
