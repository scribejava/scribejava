package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.OutputStream;

public class MicrosoftAzureActiveDirectoryService extends OAuth20Service {

    private static final String ACCEPTED_FORMAT
            = "application/json; odata=minimalmetadata; streaming=true; charset=utf-8";

    /**
     * @deprecated use {@link #MicrosoftAzureActiveDirectoryService(com.github.scribejava.core.builder.api.DefaultApi20,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, com.github.scribejava.core.httpclient.HttpClientConfig,
     * com.github.scribejava.core.httpclient.HttpClient)}
     */
    @Deprecated
    public MicrosoftAzureActiveDirectoryService(DefaultApi20 api, String apiKey, String apiSecret, String callback,
            String scope, OutputStream debugStream, String state, String responseType, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        this(api, apiKey, apiSecret, callback, scope, state, responseType, userAgent, httpClientConfig, httpClient);
    }

    public MicrosoftAzureActiveDirectoryService(DefaultApi20 api, String apiKey, String apiSecret, String callback,
            String scope, String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, scope, state, responseType, userAgent, httpClientConfig, httpClient);
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        super.signRequest(accessToken, request);
        request.addHeader("Accept", ACCEPTED_FORMAT);
    }
}
