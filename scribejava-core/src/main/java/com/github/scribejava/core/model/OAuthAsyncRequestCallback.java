package com.github.scribejava.core.model;

public interface OAuthAsyncRequestCallback<T> {

    void onCompleted(T response);

    void onThrowable(Throwable t);
}
