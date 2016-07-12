package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.model.HttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

public class NingHttpClientConfig implements HttpClient.Config {

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
}
