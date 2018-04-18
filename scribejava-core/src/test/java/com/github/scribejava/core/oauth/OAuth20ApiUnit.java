package com.github.scribejava.core.oauth;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.OAuth2SignatureType;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthConfig;
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
        return new OAuth20ServiceUnit(this, apiKey, apiSecret, callback, scope, debugStream, state, responseType,
                userAgent, httpClientConfig, httpClient);
    }

    /**
     * @deprecated use {@link #createService(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.io.OutputStream, java.lang.String, java.lang.String, java.lang.String,
     * com.github.scribejava.core.httpclient.HttpClientConfig, com.github.scribejava.core.httpclient.HttpClient)}
     */
    @Deprecated
    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return createService(config.getApiKey(), config.getApiSecret(), config.getCallback(), config.getScope(),
                config.getDebugStream(), config.getState(), config.getResponseType(), config.getUserAgent(),
                config.getHttpClientConfig(), config.getHttpClient());
    }

    @Override
    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_URI_QUERY_PARAMETER;
    }
}
