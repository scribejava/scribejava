package com.github.scribejava.httpclient.loopj;

import com.loopj.android.http.RequestHandle;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* Wrapper for Java Future -- AsyncHttpClient already does stuff in the background */
public class AsyncHttpFuture<T> implements Future<T> {

    private final RequestHandle mRequestHandle;

    public AsyncHttpFuture(RequestHandle requestHandle) {
        this.mRequestHandle = requestHandle;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.mRequestHandle.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return this.mRequestHandle.isCancelled();
    }

    @Override
    public boolean isDone() {
        return this.mRequestHandle.isFinished();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

}
