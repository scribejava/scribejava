package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.ClientAuthenticationType;
import com.github.scribejava.core.builder.api.DefaultApi20;

public class LinkedInApi20 extends DefaultApi20 {

    protected LinkedInApi20() {
    }

    private static class InstanceHolder {
        private static final LinkedInApi20 INSTANCE = new LinkedInApi20();
    }

    public static LinkedInApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.linkedin.com/oauth/v2/accessToken";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://www.linkedin.com/oauth/v2/authorization";
    }

    @Override
    public ClientAuthenticationType getClientAuthenticationType() {
        return ClientAuthenticationType.REQUEST_BODY;
    }
}
