package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.Verb;

public class KaixinApi extends DefaultApi10a {

    private static final String REQUEST_TOKEN_URL = "http://api.kaixin001.com/oauth/request_token";
    private static final String ACCESS_TOKEN_URL = "http://api.kaixin001.com/oauth/access_token";
    private static final String AUTHORIZE_URL = "http://api.kaixin001.com/oauth/authorize?oauth_token=%s";

    protected KaixinApi() {
    }

    private static class InstanceHolder {
        private static final KaixinApi INSTANCE = new KaixinApi();
    }

    public static KaixinApi instance() {
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

    @Override
    public Verb getRequestTokenVerb() {
        return Verb.GET;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }
}
