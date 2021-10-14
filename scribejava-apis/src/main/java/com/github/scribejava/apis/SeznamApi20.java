package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

public class SeznamApi20 extends DefaultApi20 {

    protected SeznamApi20() {
    }

    public static SeznamApi20 instance() {
        return SeznamApi20.InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://login.szn.cz/api/v1/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://login.szn.cz/api/v1/oauth/auth";
    }

    @Override
    public String getRevokeTokenEndpoint() {
        return "https://login.szn.cz/api/v1/oauth/revoke";
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }

    private static class InstanceHolder {

        private static final SeznamApi20 INSTANCE = new SeznamApi20();
    }

}