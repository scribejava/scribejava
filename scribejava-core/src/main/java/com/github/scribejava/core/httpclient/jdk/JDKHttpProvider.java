package com.github.scribejava.core.httpclient.jdk;

import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.httpclient.HttpClientProvider;

public class JDKHttpProvider implements HttpClientProvider {

    @Override
    public HttpClient createClient(HttpClientConfig config) {
        if (config instanceof JDKHttpClientConfig) {
            return new JDKHttpClient((JDKHttpClientConfig) config);
        }
        return null;
    }
}
