package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuthService;
import java.io.OutputStream;

/**
 * @param <T> T
 * @deprecated there is no common in Api10a and Api20. Use {@link DefaultApi10a} or {@link DefaultApi20}.<br>
 * if you need the common parent, it is the {@link Object}
 */
@Deprecated
public interface BaseApi<T extends OAuthService> {
    /**
     *
     * @param apiKey apiKey
     * @param apiSecret apiSecret
     * @param callback callback
     * @param scope scope
     * @param debugStream debugStream
     * @param responseType responseType
     * @param userAgent userAgent
     * @param httpClientConfig httpClientConfig
     * @param httpClient httpClient
     * @return service
     * @deprecated use {@link DefaultApi10a#createService(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.io.OutputStream, java.lang.String, com.github.scribejava.core.httpclient.HttpClientConfig,
     * com.github.scribejava.core.httpclient.HttpClient) } or {@link DefaultApi20#createService(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * com.github.scribejava.core.httpclient.HttpClientConfig, com.github.scribejava.core.httpclient.HttpClient)}
     */
    @Deprecated
    T createService(String apiKey, String apiSecret, String callback, String scope, OutputStream debugStream,
            String responseType, String userAgent, HttpClientConfig httpClientConfig, HttpClient httpClient);
}
