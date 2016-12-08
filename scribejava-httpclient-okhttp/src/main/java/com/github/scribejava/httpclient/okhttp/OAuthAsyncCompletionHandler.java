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

class OAuthAsyncCompletionHandler<T> implements Callback {

    private final OAuthAsyncRequestCallback<T> callback;
    private final OAuthRequestAsync.ResponseConverter<T> converter;
    private final OkHttpFuture<T> okHttpFuture;

    OAuthAsyncCompletionHandler(OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter, OkHttpFuture<T> okHttpFuture) {
        this.callback = callback;
        this.converter = converter;
        this.okHttpFuture = okHttpFuture;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        try {
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
            okHttpFuture.setResult(t);
            if (callback != null) {
                callback.onCompleted(t);
            }
        } finally {
            okHttpFuture.finish();
        }
    }
}
