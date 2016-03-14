package com.github.scribejava.core.oauth;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class CompletedFuture<V> implements Future<V> {
    private final V result;

    CompletedFuture(V result) {
        this.result = result;
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
    public V get() {
        return result;
    }

    @Override
    public V get(long timeout, TimeUnit unit) {
        return result;
    }
}
