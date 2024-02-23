package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

/**
 * Airtable.com API
 */
public class AirtableApi extends DefaultApi20 {

    protected AirtableApi() {
    }

    private static class InstanceHolder {
        private static final AirtableApi INSTANCE = new AirtableApi();
    }

    public static AirtableApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://airtable.com/oauth2/v1/token";
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return "https://airtable.com/oauth2/v1/authorize";
    }
}