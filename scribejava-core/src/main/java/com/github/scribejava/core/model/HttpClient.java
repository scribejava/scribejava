package com.github.scribejava.core.model;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

public interface HttpClient {
    void close() throws IOException;

    <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
                               String bodyContents, OAuthAsyncRequestCallback<T> callback,
                               OAuthRequestAsync.ResponseConverter<T> converter);

    interface Config {
    }
}
