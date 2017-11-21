package com.github.scribejava.core.httpclient.jdk;

import com.github.scribejava.core.AbstractClientTest;
import com.github.scribejava.core.httpclient.HttpClient;

public class JDKHttpClientTest extends AbstractClientTest {

    @Override
    protected HttpClient createNewClient() {
        return new JDKHttpClient();
    }
}
