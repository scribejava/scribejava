package com.github.scribejava.httpclient.okhttp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class OAuthAsyncCompletionHandlerTest {

    private static final AllGoodResponseConverter ALL_GOOD_RESPONSE_CONVERTER = new AllGoodResponseConverter();
    private static final ExceptionResponseConverter EXCEPTION_RESPONSE_CONVERTER = new ExceptionResponseConverter();
    private static final OAuthExceptionResponseConverter OAUTH_EXCEPTION_RESPONSE_CONVERTER
            = new OAuthExceptionResponseConverter();

    private OAuthAsyncCompletionHandler<String> handler;
    private Call call;
    private OkHttpFuture<String> future;
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
        call = new MockCall();
        future = new OkHttpFuture<>(call);
    }

    @Test
    public void shouldReleaseLatchOnSuccess() throws Exception {
        handler = new OAuthAsyncCompletionHandler<>(callback, ALL_GOOD_RESPONSE_CONVERTER, future);
        call.enqueue(handler);

        final okhttp3.Request request = new Request.Builder().url("http://localhost/").build();
        final okhttp3.Response response = new okhttp3.Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("ok")
                .body(ResponseBody.create(MediaType.get("text/plain"), new byte[0]))
                .build();
        handler.onResponse(call, response);
        assertNotNull(callback.getResponse());
        assertNull(callback.getThrowable());
        // verify latch is released
        assertEquals("All good", future.get());
    }

    @Test
    public void shouldReleaseLatchOnIOException() throws Exception {
        handler = new OAuthAsyncCompletionHandler<>(callback, EXCEPTION_RESPONSE_CONVERTER, future);
        call.enqueue(handler);

        final okhttp3.Request request = new Request.Builder().url("http://localhost/").build();
        final okhttp3.Response response = new okhttp3.Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("ok")
                .body(ResponseBody.create(MediaType.get("text/plain"), new byte[0]))
                .build();
        handler.onResponse(call, response);
        assertNull(callback.getResponse());
        assertNotNull(callback.getThrowable());
        assertTrue(callback.getThrowable() instanceof IOException);
        // verify latch is released
        try {
            future.get();
            fail();
        } catch (ExecutionException expected) {
            // expected
        }
    }

    @Test
    public void shouldReportOAuthException() throws Exception {
        handler = new OAuthAsyncCompletionHandler<>(callback, OAUTH_EXCEPTION_RESPONSE_CONVERTER, future);
        call.enqueue(handler);

        final okhttp3.Request request = new Request.Builder().url("http://localhost/").build();
        final okhttp3.Response response = new okhttp3.Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("ok")
                .body(ResponseBody.create(MediaType.get("text/plain"), new byte[0]))
                .build();
        handler.onResponse(call, response);
        assertNull(callback.getResponse());
        assertNotNull(callback.getThrowable());
        assertTrue(callback.getThrowable() instanceof OAuthException);
        // verify latch is released
        try {
            future.get();
            fail();
        } catch (ExecutionException expected) {
            // expected
        }
    }

    @Test
    public void shouldReleaseLatchOnCancel() throws Exception {
        handler = new OAuthAsyncCompletionHandler<>(callback, ALL_GOOD_RESPONSE_CONVERTER, future);
        call.enqueue(handler);

        future.cancel(true);
        assertNull(callback.getResponse());
        assertNotNull(callback.getThrowable());
        assertTrue(callback.getThrowable() instanceof IOException);
        // verify latch is released
        try {
            future.get();
            fail();
        } catch (ExecutionException expected) {
            // expected
        }
    }

    @Test
    public void shouldReleaseLatchOnFailure() throws Exception {
        handler = new OAuthAsyncCompletionHandler<>(callback, ALL_GOOD_RESPONSE_CONVERTER, future);
        call.enqueue(handler);

        handler.onFailure(call, new IOException());
        assertNull(callback.getResponse());
        assertNotNull(callback.getThrowable());
        assertTrue(callback.getThrowable() instanceof IOException);
        // verify latch is released
        try {
            future.get();
            fail();
        } catch (ExecutionException expected) {
            // expected
        }
    }

    private static class AllGoodResponseConverter implements OAuthRequest.ResponseConverter<String> {

        @Override
        public String convert(Response response) throws IOException {
            return "All good";
        }
    }

    private static class ExceptionResponseConverter implements OAuthRequest.ResponseConverter<String> {

        @Override
        public String convert(Response response) throws IOException {
            throw new IOException("Failed to convert");
        }
    }

    private static class OAuthExceptionResponseConverter implements OAuthRequest.ResponseConverter<String> {

        @Override
        public String convert(Response response) throws IOException {
            throw new OAuthException("bad oauth");
        }
    }
}
