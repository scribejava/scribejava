package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.Verb;

/**
 * @see <a href="http://apidocs.mendeley.com/home/authentication">http://apidocs.mendeley.com/home/authentication</a>
 */
public class MendeleyApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "http://api.mendeley.com/oauth/authorize?oauth_token=%s";

    protected MendeleyApi() {
    }

    private static class InstanceHolder {
        private static final MendeleyApi INSTANCE = new MendeleyApi();
    }

    public static MendeleyApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "http://api.mendeley.com/oauth/request_token/";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://api.mendeley.com/oauth/access_token/";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public Verb getRequestTokenVerb() {
        return Verb.GET;
    }
}
