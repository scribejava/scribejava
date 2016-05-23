package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.Verb;

public class LiveApi extends DefaultApi20 {

    protected LiveApi() {
    }

    private static class InstanceHolder {
        private static final LiveApi INSTANCE = new LiveApi();
    }

    public static LiveApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://login.live.com/oauth20_token.srf";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://oauth.live.com/authorize";
    }
}
