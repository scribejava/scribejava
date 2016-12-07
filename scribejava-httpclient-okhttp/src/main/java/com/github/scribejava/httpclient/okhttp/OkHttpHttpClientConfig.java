package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.model.HttpClient;
import okhttp3.OkHttpClient;

public class OkHttpHttpClientConfig implements HttpClient.Config {

    private final OkHttpClient client;

    public OkHttpHttpClientConfig(OkHttpClient client) {
        this.client = client;
    }

    public OkHttpClient getClient() {
        return client;
    }
}
