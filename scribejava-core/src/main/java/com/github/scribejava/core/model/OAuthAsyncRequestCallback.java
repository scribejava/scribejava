package com.github.scribejava.core.model;

public interface OAuthAsyncRequestCallback<T> {

    /**
     * Implementations of this method should close provided response in case it implements {@link java.io.Closeable}
     *
     * @param response response
     */
    void onCompleted(T response);

    void onThrowable(Throwable t);
}
