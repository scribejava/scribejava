package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.AbstractClientTest;
import com.github.scribejava.core.httpclient.HttpClient;

public class OkHttpHttpClientTest extends AbstractClientTest {

    @Override
    protected HttpClient createNewClient() {
        return new OkHttpHttpClient();
    }
}
