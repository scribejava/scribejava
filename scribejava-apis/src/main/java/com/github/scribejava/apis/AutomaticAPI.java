package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.ClientAuthenticationType;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

public class AutomaticAPI extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://accounts.automatic.com/oauth/authorize";
    private static final String REFRESH_TOKEN_ENDPOINT = "https://accounts.automatic.com/oauth/refresh_token";
    private static final String ACCESS_TOKEN_ENDPOINT = "https://accounts.automatic.com/oauth/access_token";

    protected AutomaticAPI() {
    }

    private static class InstanceHolder {
        private static final AutomaticAPI INSTANCE = new AutomaticAPI();
    }

    public static AutomaticAPI instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT;
    }

    @Override
    public String getRefreshTokenEndpoint() {
        return REFRESH_TOKEN_ENDPOINT;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenJsonExtractor.instance();
    }

    @Override
    public ClientAuthenticationType getClientAuthenticationType() {
        return ClientAuthenticationType.REQUEST_BODY;
    }

}
