package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.AbstractClientTest;
import com.github.scribejava.core.httpclient.HttpClient;

public class NingHttpClientTest extends AbstractClientTest {

    @Override
    protected HttpClient createNewClient() {
        return new NingHttpClient();
    }
}
