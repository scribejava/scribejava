package com.github.scribejava.core.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class ResponseTest {

    private Response response;
    private ConnectionStub connection;

    @Before
    public void setUp() throws IOException {
        connection = new ConnectionStub();
        connection.addResponseHeader("one", "one");
        connection.addResponseHeader("two", "two");
        response = new Response(connection);
    }

    @Test
    public void shouldPopulateResponseHeaders() {
        assertEquals(2, response.getHeaders().size());
        assertEquals("one", response.getHeader("one"));
    }

    @Test
    public void shouldParseBodyContents() {
        assertEquals("contents", response.getBody());
        assertEquals(1, connection.getTimesCalledInpuStream());
    }

    @Test
    public void shouldParseBodyContentsOnlyOnce() {
        assertEquals("contents", response.getBody());
        assertEquals("contents", response.getBody());
        assertEquals("contents", response.getBody());
        assertEquals(1, connection.getTimesCalledInpuStream());
    }

    @Test
    public void shouldHandleAConnectionWithErrors() throws IOException {
        final Response errResponse = new Response(new FaultyConnection());
        assertEquals(400, errResponse.getCode());
        assertEquals("errors", errResponse.getBody());
    }

    private static class FaultyConnection extends ConnectionStub {

        private FaultyConnection() throws MalformedURLException {
            super();
        }

        @Override
        public InputStream getErrorStream() {
            return new ByteArrayInputStream("errors".getBytes());
        }

        @Override
        public int getResponseCode() throws IOException {
            return 400;
        }
    }
}
