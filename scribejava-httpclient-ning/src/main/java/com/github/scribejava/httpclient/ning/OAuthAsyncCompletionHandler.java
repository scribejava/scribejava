package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.ning.http.client.AsyncCompletionHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OAuthAsyncCompletionHandler<T> extends AsyncCompletionHandler<T> {

    private final OAuthAsyncRequestCallback<T> callback;
    private final OAuthRequest.ResponseConverter<T> converter;

    public OAuthAsyncCompletionHandler(OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {
        this.callback = callback;
        this.converter = converter;
    }

    @Override
    public T onCompleted(com.ning.http.client.Response ningResponse) {
        try {
            final Map<String, String> headersMap = new HashMap<>();

            for (Map.Entry<String, List<String>> header : ningResponse.getHeaders().entrySet()) {
                final StringBuilder value = new StringBuilder();
                for (String str : header.getValue()) {
                    value.append(str);
                }
                headersMap.put(header.getKey(), value.toString());
            }

            final Response response = new Response(ningResponse.getStatusCode(), ningResponse.getStatusText(),
                    headersMap, ningResponse.getResponseBodyAsStream());

            @SuppressWarnings("unchecked")
            final T t = converter == null ? (T) response : converter.convert(response);
            if (callback != null) {
                callback.onCompleted(t);
            }
            return t;
        } catch (IOException | RuntimeException e) {
            onThrowable(e);
            return null;
        }
    }

    @Override
    public void onThrowable(Throwable t) {
        if (callback != null) {
            callback.onThrowable(t);
        }
    }
}
