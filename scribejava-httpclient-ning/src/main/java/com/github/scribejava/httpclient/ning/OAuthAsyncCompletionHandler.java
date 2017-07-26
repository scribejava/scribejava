package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.ning.http.client.AsyncCompletionHandler;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class OAuthAsyncCompletionHandler<T> extends AsyncCompletionHandler<T> {

    private final OAuthAsyncRequestCallback<T> callback;
    private final OAuthRequest.ResponseConverter<T> converter;

    public OAuthAsyncCompletionHandler(OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {
        this.callback = callback;
        this.converter = converter;
    }

    @Override
    public T onCompleted(com.ning.http.client.Response ningResponse) throws IOException {
        final Map<String, String> headersMap = ningResponse.getHeaders().entrySet().stream()
                .collect(Collectors.toMap(header -> header.getKey(),
                        header -> header.getValue().stream().collect(Collectors.joining())));

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
