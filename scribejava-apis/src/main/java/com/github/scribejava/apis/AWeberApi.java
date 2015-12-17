package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

public class AWeberApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://auth.aweber.com/1.0/oauth/authorize?oauth_token=%s";
    private static final String REQUEST_TOKEN_ENDPOINT = "https://auth.aweber.com/1.0/oauth/request_token";
    private static final String ACCESS_TOKEN_ENDPOINT = "https://auth.aweber.com/1.0/oauth/access_token";

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_ENDPOINT;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}
