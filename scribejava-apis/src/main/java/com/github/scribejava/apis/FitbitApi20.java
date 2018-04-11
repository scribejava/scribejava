package com.github.scribejava.apis;

import com.github.scribejava.apis.fitbit.FitBitJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;


/**
 * Fitbit's OAuth2 client's implementation
 * source: https://dev.fitbit.com/docs/oauth2/
 */
public class FitbitApi20 extends DefaultApi20 {

    protected FitbitApi20() {
    }

    private static class InstanceHolder {
        private static final FitbitApi20 INSTANCE = new FitbitApi20();
    }

    public static FitbitApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.fitbit.com/oauth2/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://www.fitbit.com/oauth2/authorize";
    }

    @Override
    public FitBitJsonTokenExtractor getAccessTokenExtractor() {
        return FitBitJsonTokenExtractor.instance();
    }
}
