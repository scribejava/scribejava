package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

/**
 * Line v2.0 API
 */
public class LineApi extends DefaultApi20 {

    protected LineApi() {
    }

    private static class InstanceHolder {
        private static final LineApi INSTANCE = new LineApi();
    }

    public static LineApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.line.me/v2/oauth/accessToken";
    }

    @Override
    public String getRefreshTokenEndpoint() {
        throw new UnsupportedOperationException("Line doesn't support refershing tokens");
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://access.line.me/dialog/oauth/weblogin";
    }

}
