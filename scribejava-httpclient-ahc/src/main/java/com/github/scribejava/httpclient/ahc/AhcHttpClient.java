package com.github.scribejava.httpclient.ahc;

import com.github.scribejava.core.httpclient.AbstractAsyncOnlyHttpClient;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

import java.io.File;
import java.util.function.Consumer;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.BoundRequestBuilder;

public class AhcHttpClient extends AbstractAsyncOnlyHttpClient {

    private final AsyncHttpClient client;

    public AhcHttpClient() {
        this(AhcHttpClientConfig.defaultConfig());
    }

    public AhcHttpClient(AhcHttpClientConfig ahcConfig) {
        final AsyncHttpClientConfig clientConfig = ahcConfig.getClientConfig();
        client = clientConfig == null ? new DefaultAsyncHttpClient() : new DefaultAsyncHttpClient(clientConfig);
    }

    public AhcHttpClient(AsyncHttpClient ahcClient) {
        client = ahcClient;
    }

    @Override
    public void close() throws IOException {
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
            String completeUrl, Consumer<BoundRequestBuilder> bodySetter, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {
        final BoundRequestBuilder boundRequestBuilder;
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
