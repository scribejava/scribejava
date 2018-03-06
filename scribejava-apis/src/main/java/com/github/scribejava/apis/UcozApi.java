package com.github.scribejava.apis;

import com.github.scribejava.core.extractors.OAuth1AccessTokenJSONExtractor;
import com.github.scribejava.core.extractors.OAuth1RequestTokenJSONExtractor;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class UcozApi extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "http://uapi.ucoz.com/accounts/oauthauthorizetoken";

    protected UcozApi() {
    }

    private static class InstanceHolder {
        private static final UcozApi INSTANCE = new UcozApi();
    }

    public static UcozApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint(){
        return "http://uapi.ucoz.com/accounts/oauthgetaccesstoken";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "http://uapi.ucoz.com/accounts/oauthgetrequesttoken";
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }

    @Override
    public TokenExtractor<OAuth1AccessToken> getAccessTokenExtractor() {
           return OAuth1AccessTokenJSONExtractor.instance();
    }

    @Override
    public TokenExtractor<OAuth1RequestToken> getRequestTokenExtractor() {
        return OAuth1RequestTokenJSONExtractor.instance();
    }
}
