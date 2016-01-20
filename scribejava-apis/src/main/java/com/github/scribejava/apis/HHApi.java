package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.AccessTokenExtractor;
import com.github.scribejava.core.extractors.JsonTokenExtractor;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Verb;

import com.github.scribejava.apis.service.HHOAuthServiceImpl;
import com.github.scribejava.core.oauth.OAuth20Service;

public class HHApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://m.hh.ru/oauth/authorize?response_type=code&client_id=%s";
    private static final String TOKEN_URL = "https://m.hh.ru/oauth/token?grant_type=" + OAuthConstants.AUTHORIZATION_CODE;

    private HHApi() {
    }

    private static class InstanceHolder {
        private static final HHApi INSTANCE = new HHApi();
    }

    public static HHApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        return String.format(AUTHORIZE_URL, config.getApiKey());
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override
    public OAuth20Service createService(final OAuthConfig config) {
        return new HHOAuthServiceImpl(this, config);
    }
}
