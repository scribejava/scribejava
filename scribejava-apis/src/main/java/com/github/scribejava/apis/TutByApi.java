package com.github.scribejava.apis;

import com.github.scribejava.apis.service.TutByOAuthService;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthConfig;
import java.io.OutputStream;

public class TutByApi extends DefaultApi20 {

    protected TutByApi() {
    }

    private static class InstanceHolder {
        private static final TutByApi INSTANCE = new TutByApi();
    }

    public static TutByApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://profile.tut.by/getToken";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "http://profile.tut.by/auth";
    }

    @Override
    public TutByOAuthService createService(String apiKey, String apiSecret, String callback, String scope,
            OutputStream debugStream, String state, String responseType, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new TutByOAuthService(this, apiKey, apiSecret, callback, scope, debugStream, state, responseType,
                userAgent, httpClientConfig, httpClient);
    }

    /**
     * @deprecated use {@link #createService(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.io.OutputStream, java.lang.String, java.lang.String, java.lang.String,
     * com.github.scribejava.core.httpclient.HttpClientConfig, com.github.scribejava.core.httpclient.HttpClient)}
     */
    @Deprecated
    @Override
    public TutByOAuthService createService(OAuthConfig config) {
        return createService(config.getApiKey(), config.getApiSecret(), config.getCallback(), config.getScope(),
                config.getDebugStream(), config.getState(), config.getResponseType(), config.getUserAgent(),
                config.getHttpClientConfig(), config.getHttpClient());
    }
}
