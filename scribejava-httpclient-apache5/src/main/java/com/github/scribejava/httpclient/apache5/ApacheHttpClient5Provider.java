package com.github.scribejava.httpclient.apache5;

import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.httpclient.HttpClientProvider;

public class ApacheHttpClient5Provider implements HttpClientProvider {

    @Override
    public HttpClient createClient(HttpClientConfig httpClientConfig) {
        if (httpClientConfig instanceof ApacheHttpClient5Config) {
            return new ApacheHttpClient5((ApacheHttpClient5Config) httpClientConfig);
        }
        return null;
    }
}
