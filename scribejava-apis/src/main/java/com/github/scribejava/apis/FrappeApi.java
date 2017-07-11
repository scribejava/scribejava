package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

import java.io.IOException;

public class FrappeApi extends DefaultApi20 {

    String SERVER_URL = "https://erpnext.org";

    protected FrappeApi() {
    }

    private static class InstanceHolder {
        private static final FrappeApi INSTANCE = new FrappeApi();
    }

    public static FrappeApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return this.SERVER_URL + "/api/method/frappe.integrations.oauth2.get_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return this.SERVER_URL + "/api/method/frappe.integrations.oauth2.authorize";
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return FrappeJsonTokenExtractor.instance();
    }
}
