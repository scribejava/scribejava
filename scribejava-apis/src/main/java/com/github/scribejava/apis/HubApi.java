package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class HubApi extends DefaultApi20 {
    private static HubApi instance;
    private final String serviceUrl;

    protected HubApi(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public static HubApi instance(String serviceUrl) {
        if (instance == null) {
            instance = new HubApi(serviceUrl);
        }
        return instance;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return serviceUrl + "/api/rest/oauth2/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return serviceUrl + "/api/rest/oauth2/auth";
    }

}
