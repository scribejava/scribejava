package com.github.scribejava.httpclient.apache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.concurrent.FutureCallback;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest.ResponseConverter;
import com.github.scribejava.core.model.Response;
import java.io.IOException;

public class OAuthAsyncCompletionHandler<T> implements FutureCallback<HttpResponse> {

    private final OAuthAsyncRequestCallback<T> callback;
    private final ResponseConverter<T> converter;
    private final CountDownLatch latch;
    private T result;
    private Exception exception;

    public OAuthAsyncCompletionHandler(OAuthAsyncRequestCallback<T> callback, ResponseConverter<T> converter) {
        this.callback = callback;
        this.converter = converter;
        this.latch = new CountDownLatch(1);
    }

    @Override
    public void completed(HttpResponse httpResponse) {
        try {
            final Map<String, String> headersMap = new HashMap<>();
            for (Header header : httpResponse.getAllHeaders()) {
                headersMap.put(header.getName(), header.getValue());
            }

            final StatusLine statusLine = httpResponse.getStatusLine();

            final Response response = new Response(statusLine.getStatusCode(), statusLine.getReasonPhrase(), headersMap,
                    httpResponse.getEntity().getContent());

            @SuppressWarnings("unchecked")
            final T t = converter == null ? (T) response : converter.convert(response);
            result = t;
            if (callback != null) {
                callback.onCompleted(result);
            }
        } catch (IOException | RuntimeException e) {
            exception = e;
            if (callback != null) {
                callback.onThrowable(e);
            }
        } finally {
            latch.countDown();
        }
    }

    @Override
    public void failed(Exception e) {
        exception = e;
        try {
            if (callback != null) {
                callback.onThrowable(e);
            }
        } finally {
            latch.countDown();
        }
    }

    @Override
    public void cancelled() {
        exception = new CancellationException();
        try {
            if (callback != null) {
                callback.onThrowable(exception);
            }
        } finally {
            latch.countDown();
        }
    }

    public T getResult() throws InterruptedException, ExecutionException {
        latch.await();
        if (exception != null) {
            throw new ExecutionException(exception);
        }
        return result;
    }

    public T getResult(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {

        if (!latch.await(timeout, unit)) {
            throw new TimeoutException();
        }
        if (exception != null) {
            throw new ExecutionException(exception);
        }
        return result;
    }
}
