package com.github.scribejava.apis;

import com.github.scribejava.apis.facebook.FacebookAccessTokenJsonExtractor;
import com.github.scribejava.apis.service.FacebookService;
import com.github.scribejava.core.builder.api.ClientAuthenticationType;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;

/**
 * Facebook API
 */
public class FacebookApi extends DefaultApi20 {

    private final String version;

    protected FacebookApi() {
        this("2.11");
    }

    protected FacebookApi(String version) {
        this.version = version;
    }

    private static class InstanceHolder {

        private static final FacebookApi INSTANCE = new FacebookApi();
    }

    public static FacebookApi instance() {
        return InstanceHolder.INSTANCE;
    }

    public static FacebookApi customVersion(String version) {
        return new FacebookApi(version);
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://graph.facebook.com/v" + version + "/oauth/access_token";
    }

    @Override
    public String getRefreshTokenEndpoint() {
        throw new UnsupportedOperationException("Facebook doesn't support refreshing tokens");
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://www.facebook.com/v" + version + "/dialog/oauth";
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return FacebookAccessTokenJsonExtractor.instance();
    }

    @Override
    public ClientAuthenticationType getClientAuthenticationType() {
        return ClientAuthenticationType.REQUEST_BODY;
    }

    @Override
    public FacebookService createService(OAuthConfig config) {
        return new FacebookService(this, config);
    }
}
