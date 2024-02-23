package com.github.scribejava.httpclient.apache5;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest.ResponseConverter;
import com.github.scribejava.core.model.Response;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class OAuthAsyncCompletionHandler<T> implements FutureCallback<ResponseWithEntity> {

    private final OAuthAsyncRequestCallback<T> callback;
    private final ResponseConverter<T> converter;
    private final CountDownLatch latch;
    private T result;
    private Exception exception;

    OAuthAsyncCompletionHandler(OAuthAsyncRequestCallback<T> callback, ResponseConverter<T> converter) {
        this.callback = callback;
        this.converter = converter;
        this.latch = new CountDownLatch(1);
    }

    @Override
    public void completed(ResponseWithEntity responseWithEntity) {
        try {
            final HttpResponse httpResponse = responseWithEntity.getResponse();

            final Map<String, String> headersMap = new HashMap<>();
            for (Header header : httpResponse.getHeaders()) {
                headersMap.put(header.getName(), header.getValue());
            }

            final HttpEntity entity = responseWithEntity.getEntity();
            final InputStream contentStream = entity == null ? null : entity.getContent();
            final Response response = new Response(httpResponse.getCode(), httpResponse.getReasonPhrase(), headersMap,
                    contentStream, contentStream);

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
