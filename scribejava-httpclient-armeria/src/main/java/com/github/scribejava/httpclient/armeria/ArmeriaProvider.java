package com.github.scribejava.httpclient.armeria;

import com.github.scribejava.core.httpclient.HttpClientProvider;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;

public class ArmeriaProvider implements HttpClientProvider {

    @Override
    public HttpClient createClient(HttpClientConfig config) {
        if (config instanceof ArmeriaHttpClientConfig) {
            return new ArmeriaHttpClient((ArmeriaHttpClientConfig) config);
        }
        return null;
    }
}
