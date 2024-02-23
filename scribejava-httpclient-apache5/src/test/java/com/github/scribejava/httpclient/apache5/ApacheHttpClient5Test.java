package com.github.scribejava.httpclient.apache5;

import com.github.scribejava.core.AbstractClientTest;
import com.github.scribejava.core.httpclient.HttpClient;

public class ApacheHttpClient5Test extends AbstractClientTest {

    @Override
    protected HttpClient createNewClient() {
        return new ApacheHttpClient5();
    }

}
