package com.github.scribejava.apis;

import com.github.scribejava.apis.openid.OpenIdJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;

public class ZendeskApi extends DefaultApi20 {
    
    private final String serverURL;
    private final String accessTokenEndpoint;
    private final String authorizationBaseUrl;

    protected ZendeskApi(String serverURL) {
        this.serverURL = serverURL;
        this.accessTokenEndpoint = serverURL + "/oauth/tokens";
        this.authorizationBaseUrl = serverURL + "/oauth/authorizations/new";
    }

    public static ZendeskApi instance(String serverUrl) {
        return new ZendeskApi(serverUrl);
    }

    public String getServerURL() {
        return serverURL;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return accessTokenEndpoint;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return authorizationBaseUrl;
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OpenIdJsonTokenExtractor.instance();
    }

}
