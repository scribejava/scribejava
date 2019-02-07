package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class Asana20Api extends DefaultApi20 {

    protected Asana20Api() {
    }

    private static class InstanceHolder {
        private static final Asana20Api INSTANCE = new Asana20Api();
    }

    public static Asana20Api instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://app.asana.com/-/oauth_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://app.asana.com/-/oauth_authorize";
    }
}
