package com.github.scribejava.httpclient.ahc;

import com.github.scribejava.core.model.HttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;

public class AhcHttpClientConfig implements HttpClient.Config {

    private final AsyncHttpClientConfig clientConfig;

    public AhcHttpClientConfig(AsyncHttpClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public AsyncHttpClientConfig getClientConfig() {
        return clientConfig;
    }
}
