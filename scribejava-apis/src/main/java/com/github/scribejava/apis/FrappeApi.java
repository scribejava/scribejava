package com.github.scribejava.apis;

import com.github.scribejava.apis.openid.OpenIdJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;

public class FrappeApi extends DefaultApi20 {

    private final String serverURL;
    private final String accessTokenEndpoint;
    private final String authorizationBaseUrl;

    protected FrappeApi(String serverURL) {
        this.serverURL = serverURL;
        this.accessTokenEndpoint = serverURL + "/api/method/frappe.integrations.oauth2.get_token";
        this.authorizationBaseUrl = serverURL + "/api/method/frappe.integrations.oauth2.authorize";
    }

    public static FrappeApi instance(String serverUrl) {
        return new FrappeApi(serverUrl);
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
