package com.github.scribejava.httpclient.apache;

import com.github.scribejava.core.AbstractClientTest;
import com.github.scribejava.core.httpclient.HttpClient;

public class ApacheHttpClientTest extends AbstractClientTest {

    @Override
    protected HttpClient createNewClient() {
        return new ApacheHttpClient();
    }

}
