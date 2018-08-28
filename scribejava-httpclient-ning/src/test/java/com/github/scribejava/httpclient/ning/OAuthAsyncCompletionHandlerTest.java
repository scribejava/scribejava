package com.github.scribejava.httpclient.ning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.ning.http.client.FluentCaseInsensitiveStringsMap;

public class OAuthAsyncCompletionHandlerTest {

    private static final AllGoodResponseConverter ALL_GOOD_RESPONSE_CONVERTER = new AllGoodResponseConverter();
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

        final com.ning.http.client.Response response
                = new MockResponse(200, "ok", new FluentCaseInsensitiveStringsMap(), new byte[0]);
        handler.onCompleted(response);
        assertNotNull(callback.getResponse());
        assertNull(callback.getThrowable());
        // verify latch is released
        assertEquals("All good", callback.getResponse());
    }

    @Test
    public void shouldReportOAuthException() throws Exception {
        handler = new OAuthAsyncCompletionHandler<>(callback, OAUTH_EXCEPTION_RESPONSE_CONVERTER);

        final com.ning.http.client.Response response
                = new MockResponse(200, "ok", new FluentCaseInsensitiveStringsMap(), new byte[0]);
        handler.onCompleted(response);
        assertNull(callback.getResponse());
        assertNotNull(callback.getThrowable());
        assertTrue(callback.getThrowable() instanceof OAuthException);
    }

    private static class AllGoodResponseConverter implements OAuthRequest.ResponseConverter<String> {

        @Override
        public String convert(Response response) throws IOException {
            return "All good";
        }
    }

    private static class OAuthExceptionResponseConverter implements OAuthRequest.ResponseConverter<String> {

        @Override
        public String convert(Response response) throws IOException {
            throw new OAuthException("bad oauth");
        }
    }
}
