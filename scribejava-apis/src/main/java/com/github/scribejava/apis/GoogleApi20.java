package com.github.scribejava.apis;

import com.github.scribejava.apis.google.GoogleDeviceAuthorizationJsonExtractor;
import com.github.scribejava.apis.openid.OpenIdJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.DeviceAuthorizationJsonExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;

public class GoogleApi20 extends DefaultApi20 {

    protected GoogleApi20() {
    }

    private static class InstanceHolder {

        private static final GoogleApi20 INSTANCE = new GoogleApi20();
    }

    public static GoogleApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://oauth2.googleapis.com/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth";
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OpenIdJsonTokenExtractor.instance();
    }

    @Override
    public String getRevokeTokenEndpoint() {
        return "https://oauth2.googleapis.com/revoke";
    }

    @Override
    public String getDeviceAuthorizationEndpoint() {
        return "https://oauth2.googleapis.com/device/code";
    }

    @Override
    public DeviceAuthorizationJsonExtractor getDeviceAuthorizationExtractor() {
        return GoogleDeviceAuthorizationJsonExtractor.instance();
    }
}
