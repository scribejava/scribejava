package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

public class DataportenApi extends DefaultApi20 {

    protected DataportenApi() {
    }

    private static class InstanceHolder {
        private static final DataportenApi INSTANCE = new DataportenApi();
    }

    public static DataportenApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://auth.dataporten.no/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://auth.dataporten.no/oauth/authorization";
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenJsonExtractor.instance();
    }
}
