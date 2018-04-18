package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuthService;
import java.io.OutputStream;

public interface BaseApi<T extends OAuthService> {

    /**
     * @deprecated use {@link #createService(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.io.OutputStream, java.lang.String, java.lang.String, java.lang.String,
     * com.github.scribejava.core.httpclient.HttpClientConfig, com.github.scribejava.core.httpclient.HttpClient)}
     */
    @Deprecated
    T createService(OAuthConfig config);

    T createService(String apiKey, String apiSecret, String callback, String scope, OutputStream debugStream,
            String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient);
}
