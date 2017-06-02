package com.github.scribejava.httpclient.loopj;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class OAuthAsyncCompletionHandler<T> extends AsyncHttpResponseHandler {

    private final OAuthAsyncRequestCallback<T> callback;
    private final OAuthRequest.ResponseConverter<T> converter;

    public OAuthAsyncCompletionHandler(OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {
        this.callback = callback;
        this.converter = converter;
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        if (callback != null) {
            callback.onThrowable(error);
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        final Map<String, String> headersMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            headersMap.put(headers[i].getName(), headers[i].getValue());
        }

        /* AsyncHttpClient doesn't return the HTTP status message */
        final Response response = new Response(statusCode, "", headersMap, new String(responseBody));

        try {
            final T t = converter == null ? (T) response : converter.convert(response);
            if (callback != null) {
                callback.onCompleted(t);
            }
        } catch (Exception e) {
            callback.onThrowable(e);
        }
    }

};
