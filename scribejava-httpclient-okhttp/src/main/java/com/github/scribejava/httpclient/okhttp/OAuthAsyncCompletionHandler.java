package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import okhttp3.Call;
import okhttp3.Callback;

import java.io.IOException;

class OAuthAsyncCompletionHandler<T> implements Callback {

    private final OAuthAsyncRequestCallback<T> callback;
    private final OAuthRequest.ResponseConverter<T> converter;
    private final OkHttpFuture<T> okHttpFuture;

    OAuthAsyncCompletionHandler(OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter,
            OkHttpFuture<T> okHttpFuture) {
        this.callback = callback;
        this.converter = converter;
        this.okHttpFuture = okHttpFuture;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        try {
            this.okHttpFuture.setError(e);
            if (callback != null) {
                callback.onThrowable(e);
            }
        } finally {
            okHttpFuture.finish();
        }
    }

    @Override
    public void onResponse(Call call, okhttp3.Response okHttpResponse) throws IOException {
        try {

            final Response response = OkHttpHttpClient.convertResponse(okHttpResponse);
           try {
                @SuppressWarnings("unchecked")
                final T t = converter == null ? (T) response : converter.convert(response);
                okHttpFuture.setResult(t);
                if (callback != null) {
                    callback.onCompleted(t);
                }
            } catch (Throwable t) {
                okHttpFuture.setError(t);
                if (callback != null) {
                    callback.onThrowable(t);
                }
            }
        } finally {
            okHttpFuture.finish();
        }
    }
}
