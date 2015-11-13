package com.github.scribejava.core.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionStub extends HttpURLConnection {

    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, List<String>> responseHeaders = new HashMap<String, List<String>>();
    private int inputStreamCalled = 0;

    public ConnectionStub() throws Exception {
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
        inputStreamCalled++;
        return new ByteArrayInputStream("contents".getBytes());
    }

    public int getTimesCalledInpuStream() {
        return inputStreamCalled;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("contents".getBytes());
        return baos;
    }

    @Override
    public Map<String, List<String>> getHeaderFields() {
        return responseHeaders;
    }

    public void addResponseHeader(String key, String value) {
        responseHeaders.put(key, Arrays.asList(value));
    }

    public void connect() throws IOException {
    }

    public void disconnect() {
    }

    public boolean usingProxy() {
        return false;
    }

}
