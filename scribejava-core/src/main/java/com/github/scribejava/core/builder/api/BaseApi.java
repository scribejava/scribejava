package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuthService;
import java.io.OutputStream;

public interface BaseApi<T extends OAuthService> {

    /**
     * @param apiKey apiKey
     * @param apiSecret apiSecret
     * @param callback callback
     * @param scope scope
     * @param debugStream debugStream
     * @param state state
     * @param responseType responseType
     * @param userAgent userAgent
     * @param httpClientConfig httpClientConfig
     * @param httpClient httpClient
     * @return return
     * @deprecated use one of getAuthorizationUrl method in {@link com.github.scribejava.core.oauth.OAuth20Service}
     */
    @Deprecated
    T createService(String apiKey, String apiSecret, String callback, String scope, OutputStream debugStream,
            String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient);

    T createService(String apiKey, String apiSecret, String callback, String scope, OutputStream debugStream,
            String responseType, String userAgent, HttpClientConfig httpClientConfig, HttpClient httpClient);
}
