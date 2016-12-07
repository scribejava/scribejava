package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.httpclient.HttpClientProvider;
import com.github.scribejava.core.model.HttpClient;

public class OkHttpProvider implements HttpClientProvider {

    @Override
    public HttpClient createClient(HttpClient.Config config) {
        if (config instanceof OkHttpHttpClientConfig) {
            return new OkHttpHttpClient((OkHttpHttpClientConfig) config);
        }
        return null;
    }
}
