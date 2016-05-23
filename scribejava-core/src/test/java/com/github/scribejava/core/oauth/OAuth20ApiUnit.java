package com.github.scribejava.core.oauth;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;

class OAuth20ApiUnit extends DefaultApi20 {

    @Override
    public String getAccessTokenEndpoint() {
        return "http://localhost:8080/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "http://localhost:8080/authorize";
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new OAuth20ServiceUnit(this, config);
    }
}
