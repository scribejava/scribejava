package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

public class AutomaticAPI extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://accounts.automatic.com/oauth/authorize";
    private static final String REFRESH_TOKEN_ENDPOINT = "https://accounts.automatic.com/oauth/refresh_token";
    private static final String ACCESS_TOKEN_ENDPOINT = "https://accounts.automatic.com/oauth/access_token";

    protected AutomaticAPI() {
    }

    private static class InstanceHolder {

        private static final AutomaticAPI INSTANCE = new AutomaticAPI();
    }

    public static AutomaticAPI instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT;
    }

    @Override
    public String getRefreshTokenEndpoint() {
        return REFRESH_TOKEN_ENDPOINT;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }
}
