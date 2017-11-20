package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.AbstractClientTest;
import com.github.scribejava.core.httpclient.HttpClient;
import okhttp3.OkHttpClient;

public class OkHttpHttpClientTest extends AbstractClientTest {

    @Override
    protected HttpClient createNewClient() {
        return new OkHttpHttpClient(new OkHttpClient());
    }
}
