package com.github.scribejava.apis;

import com.github.scribejava.apis.mailru.MailruOAuthService;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import java.io.OutputStream;

public class MailruApi extends DefaultApi20 {

    protected MailruApi() {
    }

    private static class InstanceHolder {
        private static final MailruApi INSTANCE = new MailruApi();
    }

    public static MailruApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://connect.mail.ru/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://connect.mail.ru/oauth/authorize";
    }

    /**
     *
     * @param apiKey apiKey
     * @param apiSecret apiSecret
     * @param callback callback
     * @param defaultScope defaultScope
     * @param debugStream debugStream
     * @param responseType responseType
     * @param userAgent userAgent
     * @param httpClientConfig httpClientConfig
     * @param httpClient httpClient
     * @return service
     * @deprecated use {@link #createService(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, com.github.scribejava.core.httpclient.HttpClientConfig,
     * com.github.scribejava.core.httpclient.HttpClient) }
     */
    @Deprecated
    @Override
    public MailruOAuthService createService(String apiKey, String apiSecret, String callback, String defaultScope,
            OutputStream debugStream, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        return createService(apiKey, apiSecret, callback, defaultScope, responseType, userAgent, httpClientConfig,
                httpClient);
    }

    @Override
    public MailruOAuthService createService(String apiKey, String apiSecret, String callback, String defaultScope,
            String responseType, String userAgent, HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new MailruOAuthService(this, apiKey, apiSecret, callback, defaultScope, responseType, userAgent,
                httpClientConfig, httpClient);
    }
}
