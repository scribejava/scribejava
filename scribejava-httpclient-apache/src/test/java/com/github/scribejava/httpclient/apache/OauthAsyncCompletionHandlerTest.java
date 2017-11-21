package com.github.scribejava.httpclient.apache;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class OauthAsyncCompletionHandlerTest {

    private OAuthAsyncCompletionHandler<String> handler;
    private TestCallback callback;

    private static class TestCallback implements OAuthAsyncRequestCallback<String> {

        private Throwable throwable;
        private String response;

        @Override
        public void onCompleted(String response) {
            this.response = response;
        }

        @Override
        public void onThrowable(Throwable throwable) {
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public String getResponse() {
            return response;
        }

    }

    @Before
    public void setUp() {
        callback = new TestCallback();
    }

    @Test
    public void shouldReleaseLatchOnSuccess() throws Exception {
        handler = new OAuthAsyncCompletionHandler<>(callback, response -> "All good");
        final HttpResponse response = new BasicHttpResponse(new BasicStatusLine(
                new ProtocolVersion("4", 1, 1), 200, "ok"));
        final BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(new byte[0]));
        response.setEntity(entity);
        handler.completed(response);
        assertNotNull(callback.getResponse());
        assertNull(callback.getThrowable());
        // verify latch is released
        assertEquals("All good", handler.getResult());
    }

    @Test
    public void shouldReleaseLatchOnIOException() throws Exception {
        handler = new OAuthAsyncCompletionHandler<>(callback, response -> {
            throw new IOException("Failed to convert");
        });
        final HttpResponse response = new BasicHttpResponse(new BasicStatusLine(
                new ProtocolVersion("4", 1, 1), 200, "ok"));
        final BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(new byte[0]));
        response.setEntity(entity);
        handler.completed(response);
        assertNull(callback.getResponse());
        assertNotNull(callback.getThrowable());
        assertTrue(callback.getThrowable() instanceof IOException);
        // verify latch is released
        try {
            handler.getResult();
            fail();
        } catch (ExecutionException expected) {
            // expected
        }
    }

    @Test
    public void shouldReleaseLatchOnCancel() throws Exception {
        handler = new OAuthAsyncCompletionHandler<>(callback, response -> "All good");
        final HttpResponse response = new BasicHttpResponse(new BasicStatusLine(
                new ProtocolVersion("4", 1, 1), 200, "ok"));
        final BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(new byte[0]));
        response.setEntity(entity);
        handler.cancelled();
        assertNull(callback.getResponse());
        assertNotNull(callback.getThrowable());
        assertTrue(callback.getThrowable() instanceof CancellationException);
        // verify latch is released
        try {
            handler.getResult();
            fail();
        } catch (ExecutionException expected) {
            // expected
        }
    }

    @Test
    public void shouldReleaseLatchOnFailure() throws Exception {
        handler = new OAuthAsyncCompletionHandler<>(callback, response -> "All good");
        final HttpResponse response = new BasicHttpResponse(new BasicStatusLine(
                new ProtocolVersion("4", 1, 1), 200, "ok"));
        final BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(new byte[0]));
        response.setEntity(entity);
        handler.failed(new RuntimeException());
        assertNull(callback.getResponse());
        assertNotNull(callback.getThrowable());
        assertTrue(callback.getThrowable() instanceof RuntimeException);
        // verify latch is released
        try {
            handler.getResult();
            fail();
        } catch (ExecutionException expected) {
            // expected
        }
    }
}
