package com.github.scribejava.core.oauth;

import java.io.OutputStream;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;

class OAuth20ApiUnit extends DefaultApi20 {

    @Override
    public String getAccessTokenEndpoint() {
        return "http://localhost:8080/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "http://localhost:8080/authorize";
    }

    @Override
    public OAuth20ServiceUnit createService(String apiKey, String apiSecret, String callback, String defaultScope,
            String responseType, OutputStream debugStream, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        return new OAuth20ServiceUnit(this, apiKey, apiSecret, callback, defaultScope, responseType, debugStream,
                userAgent, httpClientConfig, httpClient);
    }

    @Override
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
    }
}
