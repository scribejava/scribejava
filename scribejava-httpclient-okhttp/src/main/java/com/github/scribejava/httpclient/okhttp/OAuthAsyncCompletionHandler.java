package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Response;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class OAuthAsyncCompletionHandler<T> implements Callback, Future<T> {

    private final OAuthAsyncRequestCallback<T> callback;
    private final OAuthRequestAsync.ResponseConverter<T> converter;
    private final Call call;
    private final CountDownLatch latch;
    private T result;

    OAuthAsyncCompletionHandler(OAuthAsyncRequestCallback<T> callback,
                                OAuthRequestAsync.ResponseConverter<T> converter, Call call) {
        this.callback = callback;
        this.converter = converter;
        this.call = call;
        this.latch = new CountDownLatch(1);

        call.enqueue(this);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        try {
            if (callback != null) {
                callback.onThrowable(e);
            }
        } finally {
            latch.countDown();
        }
    }

    @Override
    public void onResponse(Call call, okhttp3.Response okHttpResponse) throws IOException {
        try {
            final Headers headers = okHttpResponse.headers();
            final Map<String, String> headersMap = new HashMap<>();

            for (String name : headers.names()) {
                headersMap.put(name, headers.get(name));
            }

            final Response response = new Response(okHttpResponse.code(),
                                                    okHttpResponse.message(),
                                                    headersMap,
                                                    null, // cannot return both body String and InputStream
                                                    okHttpResponse.body().byteStream());

            @SuppressWarnings("unchecked")
            final T t = converter == null ? (T) response : converter.convert(response);
            result = t;
            if (callback != null) {
                callback.onCompleted(t);
            }
        } finally {
            latch.countDown();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        call.cancel();
        return call.isCanceled();
    }

    @Override
    public boolean isCancelled() {
        return call.isCanceled();
    }

    @Override
    public boolean isDone() {
        return call.isExecuted();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        latch.await();
        return result;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        latch.await(timeout, unit);
        return result;
    }
}
