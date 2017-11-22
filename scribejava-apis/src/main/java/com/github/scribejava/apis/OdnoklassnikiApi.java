package com.github.scribejava.apis;

import com.github.scribejava.apis.service.OdnoklassnikiOAuthService;
import com.github.scribejava.core.builder.api.ClientAuthenticationType;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.OAuth2SignatureType;
import com.github.scribejava.core.model.OAuthConfig;

public class OdnoklassnikiApi extends DefaultApi20 {

    protected OdnoklassnikiApi() {
    }

    private static class InstanceHolder {
        private static final OdnoklassnikiApi INSTANCE = new OdnoklassnikiApi();
    }

    public static OdnoklassnikiApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.ok.ru/oauth/token.do";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://connect.ok.ru/oauth/authorize";
    }

    @Override
    public OdnoklassnikiOAuthService createService(OAuthConfig config) {
        return new OdnoklassnikiOAuthService(this, config);
    }

    @Override
    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_URI_QUERY_PARAMETER;
    }

    @Override
    public ClientAuthenticationType getClientAuthenticationType() {
        return ClientAuthenticationType.REQUEST_BODY;
    }
}
