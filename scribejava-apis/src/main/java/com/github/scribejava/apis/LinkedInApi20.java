package com.github.scribejava.apis;

import com.github.scribejava.apis.service.LinkedIn20ServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.OAuth2SignatureType;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

public class LinkedInApi20 extends DefaultApi20 {

    protected LinkedInApi20() {
    }

    private static class InstanceHolder {
        private static final LinkedInApi20 INSTANCE = new LinkedInApi20();
    }

    public static LinkedInApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.linkedin.com/oauth/v2/accessToken";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://www.linkedin.com/oauth/v2/authorization";
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new LinkedIn20ServiceImpl(this, config);
    }

    @Override
    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_URI_QUERY_PARAMETER;
    }
}
