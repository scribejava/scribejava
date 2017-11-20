package com.github.scribejava.httpclient.apache;

import com.github.scribejava.core.httpclient.HttpClientConfig;

public class ApacheHttpClientConfig implements HttpClientConfig {

    @Override
    public HttpClientConfig createDefaultConfig() {
        return defaultConfig();
    }

    public static ApacheHttpClientConfig defaultConfig() {
        return new ApacheHttpClientConfig();
    }
}
