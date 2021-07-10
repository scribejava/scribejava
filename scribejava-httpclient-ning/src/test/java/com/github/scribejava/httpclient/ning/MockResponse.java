package com.github.scribejava.httpclient.ning;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import com.ning.http.client.Response;
import com.ning.http.client.cookie.Cookie;
import com.ning.http.client.uri.Uri;

public class MockResponse implements Response {

    private final int statusCode;
    private final String statusText;
    private final FluentCaseInsensitiveStringsMap headers;
    private final byte[] body;

    public MockResponse(int statusCode, String statusText, FluentCaseInsensitiveStringsMap headers, byte[] body) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getStatusText() {
        return statusText;
    }

    @Override
    public byte[] getResponseBodyAsBytes() throws IOException {
        return body;
    }

    @Override
    public ByteBuffer getResponseBodyAsByteBuffer() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getResponseBodyAsStream() throws IOException {
        return new ByteArrayInputStream(getResponseBodyAsBytes());
    }

    @Override
    public String getResponseBodyExcerpt(final int maxLength, final String charset) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getResponseBodyExcerpt(final int maxLength) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getResponseBody(final String charset) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getResponseBody() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Uri getUri() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getHeader(final String name) {
        return headers.getFirstValue(name);
    }

    @Override
    public List<String> getHeaders(final String name) {
        return headers.get(name);
    }

    @Override
    public FluentCaseInsensitiveStringsMap getHeaders() {
        return headers;
    }

    @Override
    public boolean isRedirected() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Cookie> getCookies() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasResponseStatus() {
        return true;
    }

    @Override
    public boolean hasResponseHeaders() {
        return !this.headers.isEmpty();
    }

    @Override
    public boolean hasResponseBody() {
        return body != null && body.length > 0;
    }
}
