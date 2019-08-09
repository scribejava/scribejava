package com.github.scribejava.httpclient.ahc;

import com.github.scribejava.core.httpclient.HttpClientConfig;
import org.asynchttpclient.AsyncHttpClientConfig;

public class AhcHttpClientConfig implements HttpClientConfig {

    private final AsyncHttpClientConfig clientConfig;

    public AhcHttpClientConfig(AsyncHttpClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public AsyncHttpClientConfig getClientConfig() {
        return clientConfig;
    }

    @Override
    public AhcHttpClientConfig createDefaultConfig() {
        return defaultConfig();
    }

    public static AhcHttpClientConfig defaultConfig() {
        return new AhcHttpClientConfig(null);
    }
}
