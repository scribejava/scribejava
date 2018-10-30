package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

/**
 * Polar's OAuth2 client's implementation
 * source: https://www.polar.com/accesslink-api/#authentication
 */
public class PolarAPI20 extends DefaultApi20 {

    protected PolarAPI20() {
    }

    private static class InstanceHolder {
        private static final PolarAPI20 INSTANCE = new PolarAPI20();
    }

    public static PolarAPI20 instance() {
        return PolarAPI20.InstanceHolder.INSTANCE;
    }


    @Override
    public String getAccessTokenEndpoint() {
        return "https://polarremote.com/v2/oauth2/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://flow.polar.com/oauth2/authorization";
    }
}
