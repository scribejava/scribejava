package ru.hh.oauth.subscribe.core.model;

public interface OAuthAsyncRequestCallback<T> {

    void onCompleted(T response);

    void onThrowable(Throwable t);
}
