package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;

public class MicrosoftAzureActiveDirectoryService extends OAuth20Service {

    private final String acceptedFormat;

    public MicrosoftAzureActiveDirectoryService(DefaultApi20 api, String apiKey, String apiSecret, String callback,
            String scope, String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient, String acceptedFormat) {
        super(api, apiKey, apiSecret, callback, scope, state, responseType, userAgent, httpClientConfig, httpClient);
        this.acceptedFormat = acceptedFormat;
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        super.signRequest(accessToken, request);
        request.addHeader("Accept", acceptedFormat);
    }
}
