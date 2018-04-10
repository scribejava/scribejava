package com.github.scribejava.apis;

import com.github.scribejava.apis.service.Fitbit20ServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;


/**
 * Fitbit's OAuth2 client's implementation
 * source: https://dev.fitbit.com/docs/oauth2/
 * 
 * Note - this is an updated version of this library for Scribe v5.3.0. Original code here:
 * 		- https://github.com/alexthered/fitbitAPI20-scribe-java 
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
    public OAuth20Service createService(OAuthConfig config) {
        return new Fitbit20ServiceImpl(this, config);
    }
}