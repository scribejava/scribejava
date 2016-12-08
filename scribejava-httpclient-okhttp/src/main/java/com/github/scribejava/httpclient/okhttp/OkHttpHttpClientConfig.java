package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.model.HttpClient;
import okhttp3.OkHttpClient;

public class OkHttpHttpClientConfig implements HttpClient.Config {

    private final OkHttpClient.Builder clientBuilder;

    public OkHttpHttpClientConfig(OkHttpClient.Builder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    public OkHttpClient.Builder getClientBuilder() {
        return clientBuilder;
    }
}
