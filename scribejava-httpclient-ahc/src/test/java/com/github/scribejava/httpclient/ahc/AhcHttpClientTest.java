package com.github.scribejava.httpclient.ahc;

import com.github.scribejava.core.AbstractClientTest;
import com.github.scribejava.core.httpclient.HttpClient;
import org.junit.Ignore;

@Ignore(value = "we are java7 and AHC is java8")
public class AhcHttpClientTest extends AbstractClientTest {

    @Override
    protected HttpClient createNewClient() {
        return new AhcHttpClient();
    }
}
