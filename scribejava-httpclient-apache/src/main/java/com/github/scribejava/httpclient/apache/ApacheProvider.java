package com.github.scribejava.httpclient.apache;

import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.httpclient.HttpClientProvider;

public class ApacheProvider implements HttpClientProvider {

    @Override
    public HttpClient createClient(HttpClientConfig httpClientConfig) {
        if (httpClientConfig instanceof ApacheHttpClientConfig) {
            return new ApacheHttpClient((ApacheHttpClientConfig) httpClientConfig);
        }
        return null;
    }
}
