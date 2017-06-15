package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig;

public class NingHttpClientConfig implements HttpClientConfig {

    private final AsyncHttpClientConfig config;
    private String ningAsyncHttpProviderClassName;

    public NingHttpClientConfig(AsyncHttpClientConfig config) {
        this.config = config;
    }

    public String getNingAsyncHttpProviderClassName() {
        return ningAsyncHttpProviderClassName;
    }

    public void setNingAsyncHttpProviderClassName(String ningAsyncHttpProviderClassName) {
        this.ningAsyncHttpProviderClassName = ningAsyncHttpProviderClassName;
    }

    public AsyncHttpClientConfig getConfig() {
        return config;
    }

    @Override
    public NingHttpClientConfig createDefaultConfig() {
        return defaultConfig();
    }

    public static NingHttpClientConfig defaultConfig() {
        return new NingHttpClientConfig(null);
    }
}
