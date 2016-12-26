package com.github.scribejava.core.httpclient;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface HttpClient {

    void close() throws IOException;

    <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            byte[] bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter);

    <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter);

    <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequestAsync.ResponseConverter<T> converter);

    Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            byte[] bodyContents) throws InterruptedException, ExecutionException, IOException;

    Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents) throws InterruptedException, ExecutionException, IOException;

    Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents) throws InterruptedException, ExecutionException, IOException;
}
