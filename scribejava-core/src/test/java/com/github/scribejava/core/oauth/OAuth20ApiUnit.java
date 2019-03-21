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
    public OAuth20ServiceUnit createService(String apiKey, String apiSecret, String callback, String defaultScope,
            OutputStream debugStream, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        return createService(apiKey, apiSecret, callback, defaultScope, responseType, userAgent, httpClientConfig,
                httpClient);
    }

    @Override
    public OAuth20ServiceUnit createService(String apiKey, String apiSecret, String callback, String defaultScope,
            String responseType, String userAgent, HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new OAuth20ServiceUnit(this, apiKey, apiSecret, callback, defaultScope, responseType, userAgent,
                httpClientConfig, httpClient);
    }

    @Override
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
    }
}
