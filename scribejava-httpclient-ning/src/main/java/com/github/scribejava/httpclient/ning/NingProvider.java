package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.httpclient.HttpClientProvider;
import com.github.scribejava.core.model.HttpClient;

public class NingProvider implements HttpClientProvider {

    @Override
    public HttpClient createClient(HttpClient.Config httpClientConfig) {
        if (httpClientConfig instanceof NingHttpClientConfig) {
            return new NingHttpClient((NingHttpClientConfig) httpClientConfig);
        }
        return null;
    }
}
