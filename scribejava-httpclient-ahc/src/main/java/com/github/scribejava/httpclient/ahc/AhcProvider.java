package com.github.scribejava.httpclient.ahc;

import com.github.scribejava.core.httpclient.HttpClientProvider;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;

public class AhcProvider implements HttpClientProvider {

    @Override
    public HttpClient createClient(HttpClientConfig config) {
        if (config instanceof AhcHttpClientConfig) {
            return new AhcHttpClient((AhcHttpClientConfig) config);
        }
        return null;
    }
}
