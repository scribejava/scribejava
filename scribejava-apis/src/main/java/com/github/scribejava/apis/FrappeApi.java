package com.github.scribejava.apis;

import com.github.scribejava.apis.frappe.FrappeJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

public class FrappeApi extends DefaultApi20 {

    private String serverURL = "https://scribejava.mntechnique.com";

    protected FrappeApi() {
    }

    private static class InstanceHolder {
        private static final FrappeApi INSTANCE = new FrappeApi();
    }

    public static FrappeApi instance() {
        return InstanceHolder.INSTANCE;
    }

    public String getServerURL() {
        return this.serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return this.serverURL + "/api/method/frappe.integrations.oauth2.get_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return this.serverURL + "/api/method/frappe.integrations.oauth2.authorize";
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return FrappeJsonTokenExtractor.instance();
    }
}
