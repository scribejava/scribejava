package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

public class TheThingsNetworkV2PreviewApi extends DefaultApi20 {

    protected TheThingsNetworkV2PreviewApi() {
    }

    private static class InstanceHolder {
        private static final TheThingsNetworkV2PreviewApi INSTANCE = new TheThingsNetworkV2PreviewApi();
    }

    public static TheThingsNetworkV2PreviewApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://preview.account.thethingsnetwork.org/users/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://preview.account.thethingsnetwork.org/users/authorize";
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenJsonExtractor.instance();
    }
}
