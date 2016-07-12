package com.github.scribejava.core.httpclient;

import com.github.scribejava.core.model.HttpClient;

public interface HttpClientProvider {

    HttpClient createClient(HttpClient.Config httpClientConfig);
}
