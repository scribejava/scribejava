package com.github.scribejava.httpclient.okhttp;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;

public class OkHttpFuture<T> implements Future<T> {

    private final CountDownLatch latch = new CountDownLatch(1);
    private final Call call;
    private T result;
    private Exception exception;

    public OkHttpFuture(Call call) {
        this.call = call;
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

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        latch.await();
        if (exception != null) {
            throw new ExecutionException(exception);
        }
        return result;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            if (exception != null) {
                throw new ExecutionException(exception);
            }
            return result;
        }
        throw new TimeoutException();
    }

    void finish() {
        latch.countDown();
    }

    void setResult(T result) {
        this.result = result;
    }

}
