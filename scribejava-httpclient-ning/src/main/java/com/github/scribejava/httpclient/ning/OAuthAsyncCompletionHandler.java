package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Response;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OAuthAsyncCompletionHandler<T> extends AsyncCompletionHandler<T> {

    private final OAuthAsyncRequestCallback<T> callback;
    private final OAuthRequestAsync.ResponseConverter<T> converter;

    public OAuthAsyncCompletionHandler(OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {
        this.callback = callback;
        this.converter = converter;
    }

    @Override
    public T onCompleted(com.ning.http.client.Response ningResponse) throws IOException {
        final FluentCaseInsensitiveStringsMap map = ningResponse.getHeaders();
        final Map<String, String> headersMap = new HashMap<>();
        for (FluentCaseInsensitiveStringsMap.Entry<String, List<String>> header : map) {
            final StringBuilder value = new StringBuilder();
            for (String str : header.getValue()) {
                value.append(str);
            }
            headersMap.put(header.getKey(), value.toString());
        }
        final Response response = new Response(ningResponse.getStatusCode(), ningResponse.getStatusText(), headersMap,
                ningResponse.getResponseBodyAsStream());

        @SuppressWarnings("unchecked")
        final T t = converter == null ? (T) response : converter.convert(response);
        if (callback != null) {
            callback.onCompleted(t);
        }
        return t;
    }

    @Override
    public void onThrowable(Throwable t) {
        if (callback != null) {
            callback.onThrowable(t);
        }
    }
};
