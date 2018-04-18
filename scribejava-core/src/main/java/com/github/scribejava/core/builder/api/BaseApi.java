package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuthService;
import java.io.OutputStream;

public interface BaseApi<T extends OAuthService> {

    T createService(String apiKey, String apiSecret, String callback, String scope, OutputStream debugStream,
            String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient);
}
