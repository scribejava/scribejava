package com.github.scribejava.httpclient.loopj;

import com.github.scribejava.core.httpclient.HttpClientConfig;

public class AsyncHttpClientConfig implements HttpClientConfig {

    public AsyncHttpClientConfig() {
    }

    @Override
    public HttpClientConfig createDefaultConfig() {
        return new AsyncHttpClientConfig();
    }
}
