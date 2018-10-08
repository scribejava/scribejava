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

    @Override
    public MailruOAuthService createService(String apiKey, String apiSecret, String callback, String scope,
            OutputStream debugStream, String state, String responseType, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new MailruOAuthService(this, apiKey, apiSecret, callback, scope, state, responseType, userAgent,
                httpClientConfig, httpClient);
    }
}
