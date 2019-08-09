package com.github.scribejava.httpclient.ahc;

import com.github.scribejava.core.AbstractClientTest;
import com.github.scribejava.core.httpclient.HttpClient;

public class AhcHttpClientTest extends AbstractClientTest {

    @Override
    protected HttpClient createNewClient() {
        return new AhcHttpClient();
    }
}
