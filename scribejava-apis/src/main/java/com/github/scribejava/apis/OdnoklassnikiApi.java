package com.github.scribejava.apis;

import com.github.scribejava.apis.service.OdnoklassnikiOAuthService;
import com.github.scribejava.core.builder.api.ClientAuthenticationType;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.OAuth2SignatureType;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthConfig;
import java.io.OutputStream;

public class OdnoklassnikiApi extends DefaultApi20 {

    protected OdnoklassnikiApi() {
    }

    private static class InstanceHolder {
        private static final OdnoklassnikiApi INSTANCE = new OdnoklassnikiApi();
    }

    public static OdnoklassnikiApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.ok.ru/oauth/token.do";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://connect.ok.ru/oauth/authorize";
    }

    @Override
    public OdnoklassnikiOAuthService createService(String apiKey, String apiSecret, String callback, String scope,
            OutputStream debugStream, String state, String responseType, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new OdnoklassnikiOAuthService(this, apiKey, apiSecret, callback, scope, debugStream, state, responseType,
                userAgent, httpClientConfig, httpClient);
    }

    /**
     * @deprecated use {@link #createService(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.io.OutputStream, java.lang.String, java.lang.String, java.lang.String,
     * com.github.scribejava.core.httpclient.HttpClientConfig, com.github.scribejava.core.httpclient.HttpClient)}
     */
    @Deprecated
    @Override
    public OdnoklassnikiOAuthService createService(OAuthConfig config) {
        return createService(config.getApiKey(), config.getApiSecret(), config.getCallback(), config.getScope(),
                config.getDebugStream(), config.getState(), config.getResponseType(), config.getUserAgent(),
                config.getHttpClientConfig(), config.getHttpClient());
    }

    @Override
    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_URI_QUERY_PARAMETER;
    }

    @Override
    public ClientAuthenticationType getClientAuthenticationType() {
        return ClientAuthenticationType.REQUEST_BODY;
    }
}
