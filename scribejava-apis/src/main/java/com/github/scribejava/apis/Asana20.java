package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

public class Asana20 extends DefaultApi20 {
    protected Asana20() {
    }

    private static class InstanceHolder {
        private  static final Asana20 INSTANCE = new Asana20();
    }

    public static Asana20 instance() {
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

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }
}
