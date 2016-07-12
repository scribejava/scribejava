package com.github.scribejava.httpclient.ahc;

import com.github.scribejava.core.httpclient.HttpClientProvider;
import com.github.scribejava.core.model.HttpClient;

public class AhcProvider implements HttpClientProvider {

    @Override
    public HttpClient createClient(HttpClient.Config config) {
        if (config instanceof AhcHttpClientConfig) {
            return new AhcHttpClient((AhcHttpClientConfig) config);
        }
        return null;
    }
}
