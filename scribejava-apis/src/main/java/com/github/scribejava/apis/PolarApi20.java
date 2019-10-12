package com.github.scribejava.apis;

import com.github.scribejava.apis.fitbit.FitBitJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;


/**
 * Polar's OAuth2 client's implementation
 * source: https://www.polar.com/accesslink-api/#polar-accesslink-api
 */
public class PolarApi20 extends DefaultApi20 {

    protected PolarApi20() {
    }

    private static class InstanceHolder {
        private static final PolarApi20 INSTANCE = new PolarApi20();
    }

    public static PolarApi20 instance() {
        return InstanceHolder.INSTANCE;
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
