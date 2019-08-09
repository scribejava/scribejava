package com.github.scribejava.core.httpclient;

public interface HttpClientProvider {

    HttpClient createClient(HttpClientConfig httpClientConfig);
}
