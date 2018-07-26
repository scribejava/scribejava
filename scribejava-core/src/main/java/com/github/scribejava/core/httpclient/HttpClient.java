package com.github.scribejava.core.httpclient;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface HttpClient extends Closeable {

    String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";
    String CONTENT_TYPE = "Content-Type";
    String CONTENT_LENGTH = "Content-Length";

    <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            byte[] bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter);

    <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            MultipartPayload bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter);

    <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter);

    <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter);

    Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            byte[] bodyContents) throws InterruptedException, ExecutionException, IOException;

    Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            MultipartPayload bodyContents) throws InterruptedException, ExecutionException, IOException;

    Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents) throws InterruptedException, ExecutionException, IOException;

    Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents) throws InterruptedException, ExecutionException, IOException;
}
