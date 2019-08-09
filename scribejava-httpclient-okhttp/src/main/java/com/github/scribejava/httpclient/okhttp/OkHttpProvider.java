package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.httpclient.HttpClientProvider;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;

public class OkHttpProvider implements HttpClientProvider {

    @Override
    public HttpClient createClient(HttpClientConfig config) {
        if (config instanceof OkHttpHttpClientConfig) {
            return new OkHttpHttpClient((OkHttpHttpClientConfig) config);
        }
        return null;
    }
}
