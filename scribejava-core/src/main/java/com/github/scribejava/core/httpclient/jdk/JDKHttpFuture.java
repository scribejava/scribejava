package com.github.scribejava.core.httpclient.jdk;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class JDKHttpFuture<V> implements Future<V> {

    private final Exception exception;
    private final V response;

    public JDKHttpFuture(Exception exception) {
        this(null, exception);
    }

    public JDKHttpFuture(V response) {
        this(response, null);
    }

    private JDKHttpFuture(V response, Exception exception) {
        this.response = response;
        this.exception = exception;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (exception != null) {
            throw new ExecutionException(exception);
        }

        return response;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return get();
    }
}
