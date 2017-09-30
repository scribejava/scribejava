package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.AbstractClientTest;
import com.github.scribejava.core.httpclient.HttpClient;
import com.ning.http.client.AsyncHttpClient;

public class NingHttpClientTest extends AbstractClientTest {

    @Override
    protected HttpClient createNewClient() {
        return new NingHttpClient(new AsyncHttpClient());
    }
}
