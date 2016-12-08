package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.httpclient.HttpClientProvider;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;

public class NingProvider implements HttpClientProvider {

    @Override
    public HttpClient createClient(HttpClientConfig httpClientConfig) {
        if (httpClientConfig instanceof NingHttpClientConfig) {
            return new NingHttpClient((NingHttpClientConfig) httpClientConfig);
        }
        return null;
    }
}
