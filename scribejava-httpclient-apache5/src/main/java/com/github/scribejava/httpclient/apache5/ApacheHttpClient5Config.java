package com.github.scribejava.httpclient.apache5;

import com.github.scribejava.core.httpclient.HttpClientConfig;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;

public class ApacheHttpClient5Config implements HttpClientConfig {

    private final HttpAsyncClientBuilder httpAsyncClientBuilder;

    public ApacheHttpClient5Config(HttpAsyncClientBuilder httpAsyncClientBuilder) {
        this.httpAsyncClientBuilder = httpAsyncClientBuilder;
    }

    public HttpAsyncClientBuilder getHttpAsyncClientBuilder() {
        return httpAsyncClientBuilder;
    }

    @Override
    public HttpClientConfig createDefaultConfig() {
        return defaultConfig();
    }

    public static ApacheHttpClient5Config defaultConfig() {
        return new ApacheHttpClient5Config(HttpAsyncClientBuilder.create());
    }
}
