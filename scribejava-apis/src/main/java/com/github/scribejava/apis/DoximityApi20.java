package com.github.scribejava.apis;

import com.github.scribejava.apis.service.Doximity20ServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

public class DoximityApi20 extends DefaultApi20 {

    protected DoximityApi20(){
    }

    private static class InstanceHolder {
        private static final DoximityApi20 INSTANCE = new DoximityApi20();
    }

    public static final DoximityApi20 instance(){
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://auth.doximity.com/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://auth.doximity.com/oauth/authorize";
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new Doximity20ServiceImpl(this, config);
    }
}
