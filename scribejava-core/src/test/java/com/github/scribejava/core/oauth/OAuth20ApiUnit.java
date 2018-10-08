package com.github.scribejava.core.oauth;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;
import java.io.OutputStream;

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
    public OAuth20ServiceUnit createService(String apiKey, String apiSecret, String callback, String scope,
            OutputStream debugStream, String state, String responseType, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new OAuth20ServiceUnit(this, apiKey, apiSecret, callback, scope, state, responseType, userAgent,
                httpClientConfig, httpClient);
    }

    @Override
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
    }
}
