package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;

public class SapoApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://id.sapo.pt/oauth/authorize?oauth_token=%s";
    private static final String ACCESS_URL = "https://id.sapo.pt/oauth/access_token";
    private static final String REQUEST_URL = "https://id.sapo.pt/oauth/request_token";

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_URL;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_URL;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
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
