package com.github.scribejava.core.httpclient.jdk;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Fake Future. Just to have Future API for the default JDK Http client. It's NOT Async in any way. Just facade.<br>
 * That's it. Sync execution with Async methods. This class does NOT provide any async executions.
 */
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
