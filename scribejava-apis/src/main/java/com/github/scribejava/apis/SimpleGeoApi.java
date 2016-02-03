package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

/**
 * @author Pablo Fernandez
 */
public class SimpleGeoApi extends DefaultApi10a {

    private static final String ENDPOINT = "these are not used since SimpleGeo uses 2 legged OAuth";

    protected SimpleGeoApi() {
    }

    private static class InstanceHolder {
        private static final SimpleGeoApi INSTANCE = new SimpleGeoApi();
    }

    public static SimpleGeoApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return ENDPOINT;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ENDPOINT;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return ENDPOINT;
    }
}
