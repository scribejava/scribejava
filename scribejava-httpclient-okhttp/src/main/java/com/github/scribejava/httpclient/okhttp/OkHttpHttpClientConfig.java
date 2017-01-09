package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.httpclient.HttpClientConfig;
import okhttp3.OkHttpClient;

public class OkHttpHttpClientConfig implements HttpClientConfig {

    private final OkHttpClient.Builder clientBuilder;

    public OkHttpHttpClientConfig(OkHttpClient.Builder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    public OkHttpClient.Builder getClientBuilder() {
        return clientBuilder;
    }

    @Override
    public OkHttpHttpClientConfig createDefaultConfig() {
        return defaultConfig();
    }

    public static OkHttpHttpClientConfig defaultConfig() {
        return new OkHttpHttpClientConfig(null);
    }
}
