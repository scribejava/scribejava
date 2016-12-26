package com.github.scribejava.core.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionStub extends HttpURLConnection {

    private final Map<String, String> headers = new HashMap<>();

    public ConnectionStub() throws MalformedURLException {
        super(new URL("http://example.com"));
    }

    @Override
    public void setRequestProperty(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public String getRequestProperty(String s) {
        return headers.get(s);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public int getResponseCode() throws IOException {
        return 200;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream("contents".getBytes());
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("contents".getBytes());
        return baos;
    }

    @Override
    public Map<String, List<String>> getHeaderFields() {
        return Collections.emptyMap();
    }

    @Override
    public void connect() {
    }

    @Override
    public void disconnect() {
    }

    @Override
    public boolean usingProxy() {
        return false;
    }

}
