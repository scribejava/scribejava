package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.httpclient.AbstractAsyncOnlyHttpClient;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.ning.http.client.AsyncHttpClient;

import java.util.Map;
import java.util.concurrent.Future;

import com.ning.http.client.AsyncHttpClientConfig;
import java.io.File;
import java.util.function.Consumer;

public class NingHttpClient extends AbstractAsyncOnlyHttpClient {

    private final AsyncHttpClient client;

    public NingHttpClient() {
        this(NingHttpClientConfig.defaultConfig());
    }

    public NingHttpClient(NingHttpClientConfig ningConfig) {
        final String ningAsyncHttpProviderClassName = ningConfig.getNingAsyncHttpProviderClassName();
        AsyncHttpClientConfig config = ningConfig.getConfig();
        if (ningAsyncHttpProviderClassName == null) {
            client = config == null ? new AsyncHttpClient() : new AsyncHttpClient(config);
        } else {
            if (config == null) {
                config = new AsyncHttpClientConfig.Builder().build();
            }
            client = new AsyncHttpClient(ningAsyncHttpProviderClassName, config);
        }
    }

    public NingHttpClient(AsyncHttpClient client) {
        this.client = client;
    }

    @Override
    public void close() {
        client.close();
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            byte[] bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl,
                requestBuilder -> requestBuilder.setBody(bodyContents), callback, converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl,
                requestBuilder -> requestBuilder.setBody(bodyContents), callback, converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl,
                requestBuilder -> requestBuilder.setBody(bodyContents), callback, converter);
    }

    private <T> Future<T> doExecuteAsync(String userAgent, Map<String, String> headers, Verb httpVerb,
            String completeUrl, Consumer<AsyncHttpClient.BoundRequestBuilder> bodySetter,
            OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {
        final AsyncHttpClient.BoundRequestBuilder boundRequestBuilder;
        switch (httpVerb) {
            case GET:
                boundRequestBuilder = client.prepareGet(completeUrl);
                break;
            case POST:
                boundRequestBuilder = client.preparePost(completeUrl);
                break;
            case PUT:
                boundRequestBuilder = client.preparePut(completeUrl);
                break;
            case DELETE:
                boundRequestBuilder = client.prepareDelete(completeUrl);
                break;
            default:
                throw new IllegalArgumentException("message build error: unknown verb type");
        }

        if (httpVerb.isPermitBody()) {
            if (!headers.containsKey(CONTENT_TYPE)) {
                boundRequestBuilder.addHeader(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
            }
            bodySetter.accept(boundRequestBuilder);
        }

        headers.forEach((headerKey, headerValue) -> boundRequestBuilder.addHeader(headerKey, headerValue));

        if (userAgent != null) {
            boundRequestBuilder.setHeader(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }

        return boundRequestBuilder.execute(new OAuthAsyncCompletionHandler<>(callback, converter));
    }
}
