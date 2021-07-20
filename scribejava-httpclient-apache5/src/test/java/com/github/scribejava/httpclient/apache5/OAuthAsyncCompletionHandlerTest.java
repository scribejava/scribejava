package com.github.scribejava.httpclient.apache5;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.BasicHttpEntity;
import org.apache.hc.core5.http.message.BasicHttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OAuthAsyncCompletionHandlerTest {

    private static final AllGoodResponseConverter ALL_GOOD_RESPONSE_CONVERTER = new AllGoodResponseConverter();
    private static final ExceptionResponseConverter EXCEPTION_RESPONSE_CONVERTER = new ExceptionResponseConverter();
    private static final OAuthExceptionResponseConverter OAUTH_EXCEPTION_RESPONSE_CONVERTER
            = new OAuthExceptionResponseConverter();

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
        handler = new OAuthAsyncCompletionHandler<>(callback, ALL_GOOD_RESPONSE_CONVERTER);
        final HttpResponse response = new BasicHttpResponse(200, "ok");
        final BasicHttpEntity entity = new BasicHttpEntity(
                new ByteArrayInputStream(new byte[0]), ContentType.DEFAULT_BINARY);

        handler.completed(new ResponseWithEntity(response, entity));
        assertNotNull(callback.getResponse());
        assertNull(callback.getThrowable());
        // verify latch is released
        assertEquals("All good", handler.getResult());
    }

    @Test
    public void shouldReleaseLatchOnIOException() {
        handler = new OAuthAsyncCompletionHandler<>(callback, EXCEPTION_RESPONSE_CONVERTER);
        final HttpResponse response = new BasicHttpResponse(200, "ok");
        final BasicHttpEntity entity = new BasicHttpEntity(
                new ByteArrayInputStream(new byte[0]), ContentType.DEFAULT_BINARY);

        handler.completed(new ResponseWithEntity(response, entity));
        assertNull(callback.getResponse());
        assertNotNull(callback.getThrowable());
        assertTrue(callback.getThrowable() instanceof IOException);
        // verify latch is released
        assertThrows(ExecutionException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                handler.getResult();
            }
        });
    }

    @Test
    public void shouldReportOAuthException() {
        handler = new OAuthAsyncCompletionHandler<>(callback, OAUTH_EXCEPTION_RESPONSE_CONVERTER);
        final HttpResponse response = new BasicHttpResponse(200, "ok");
        final BasicHttpEntity entity = new BasicHttpEntity(
                new ByteArrayInputStream(new byte[0]), ContentType.DEFAULT_BINARY);

        handler.completed(new ResponseWithEntity(response, entity));
        assertNull(callback.getResponse());
        assertNotNull(callback.getThrowable());
        assertTrue(callback.getThrowable() instanceof OAuthException);
        // verify latch is released
        assertThrows(ExecutionException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                handler.getResult();
            }
        });
    }

    @Test
    public void shouldReleaseLatchOnCancel() {
        handler = new OAuthAsyncCompletionHandler<>(callback, ALL_GOOD_RESPONSE_CONVERTER);

        handler.cancelled();
        assertNull(callback.getResponse());
        assertNotNull(callback.getThrowable());
        assertTrue(callback.getThrowable() instanceof CancellationException);
        // verify latch is released
        assertThrows(ExecutionException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                handler.getResult();
            }
        });
    }

    @Test
    public void shouldReleaseLatchOnFailure() {
        handler = new OAuthAsyncCompletionHandler<>(callback, ALL_GOOD_RESPONSE_CONVERTER);

        handler.failed(new RuntimeException());
        assertNull(callback.getResponse());
        assertNotNull(callback.getThrowable());
        assertTrue(callback.getThrowable() instanceof RuntimeException);
        // verify latch is released
        assertThrows(ExecutionException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                handler.getResult();
            }
        });
    }

    private static class AllGoodResponseConverter implements OAuthRequest.ResponseConverter<String> {

        @Override
        public String convert(Response response) throws IOException {
            response.close();
            return "All good";
        }
    }

    private static class ExceptionResponseConverter implements OAuthRequest.ResponseConverter<String> {

        @Override
        public String convert(Response response) throws IOException {
            response.close();
            throw new IOException("Failed to convert");
        }
    }

    private static class OAuthExceptionResponseConverter implements OAuthRequest.ResponseConverter<String> {

        @Override
        public String convert(Response response) throws IOException {
            response.close();
            throw new OAuthException("bad oauth");
        }
    }
}
