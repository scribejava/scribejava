package com.github.scribejava.apis;

import com.github.scribejava.apis.zoho.ZohoService;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

import java.io.OutputStream;

public class ZohoApi20 extends DefaultApi20 {

    protected static final String ACCESS_TOKEN_ENDPOINT = "https://accounts.zoho.com/oauth/v2/token";
    protected static final String AUTHORIZATION_BASE_URL = "https://accounts.zoho.com/oauth/v2/auth";
    protected static final String REVOKE_TOKEN_ENDPOINT = "https://accounts.zoho.com/oauth/v2/token/revoke";

    protected ZohoApi20() {
    }

    private static class InstanceHolder {
        private static final ZohoApi20 INSTANCE = new ZohoApi20();
    }

    public static ZohoApi20 instance() {
        return ZohoApi20.InstanceHolder.INSTANCE;
    }


    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return AUTHORIZATION_BASE_URL;
    }

    @Override
    public String getRevokeTokenEndpoint() {
        return REVOKE_TOKEN_ENDPOINT;
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }

    @Override
    public OAuth20Service createService(String apiKey, String apiSecret, String callback, String defaultScope,
                                        String responseType, OutputStream debugStream, String userAgent,
                                        HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new ZohoService(this, apiKey, apiSecret, callback, defaultScope, responseType, debugStream,
                userAgent, httpClientConfig, httpClient);
    }
}
