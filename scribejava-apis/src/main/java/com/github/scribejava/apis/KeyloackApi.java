package com.github.scribejava.apis;

import com.github.scribejava.apis.openid.OpenIdJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.OAuth2SignatureType;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;

public class KeyloackApi extends DefaultApi20 {

    private String baseUrl = "http://localhost:8080";
    private String realm = "master";

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl + (baseUrl.endsWith("/")? "" : "/");
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    protected KeyloackApi() {
    }

    private static class InstanceHolder {
        private static final KeyloackApi INSTANCE = new KeyloackApi();
    }

    public static KeyloackApi instance() {
        return KeyloackApi.InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return baseUrl + "auth/realms/" + realm + "/protocol/openid-connect/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return baseUrl + "auth/realms/" + realm + "/protocol/openid-connect/auth";
    }

    @Override
    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_AUTHORIZATION_REQUEST_HEADER_FIELD;
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OpenIdJsonTokenExtractor.instance();
    }

    @Override
    public String getRevokeTokenEndpoint() {
        throw new RuntimeException("Not implemented yet");
    }
}
