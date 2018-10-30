package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

/**
 * Withings's OAuth2 client's implementation
 * source: http://developer.withings.com/oauth2/#tag/OAuth-2.0
 */
public class WithingsAPI20 extends DefaultApi20 {

    protected WithingsAPI20() {
    }

    private static class InstanceHolder {
        private static final WithingsAPI20 INSTANCE = new WithingsAPI20();
    }

    public static WithingsAPI20 instance() {
        return WithingsAPI20.InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://account.withings.com/oauth2/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://account.withings.com/oauth2_user/authorize2";
    }
}
