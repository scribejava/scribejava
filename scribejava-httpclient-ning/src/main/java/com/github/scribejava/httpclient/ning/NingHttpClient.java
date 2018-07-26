package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.httpclient.AbstractAsyncOnlyHttpClient;
import com.github.scribejava.core.httpclient.MultipartPayload;
import com.github.scribejava.core.java8.Consumer;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.ning.http.client.AsyncHttpClient;

import java.util.Map;
import java.util.concurrent.Future;

import com.ning.http.client.AsyncHttpClientConfig;
import java.io.File;

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
            final byte[] bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, new ByteArrayConsumer(bodyContents), callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            MultipartPayload bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {

        throw new UnsupportedOperationException("NingHttpClient does not support MultipartPayload yet.");
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            final String bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, new StringConsumer(bodyContents), callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            final File bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, new FileConsumer(bodyContents), callback,
                converter);
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

        for (Map.Entry<String, String> header : headers.entrySet()) {
            boundRequestBuilder.addHeader(header.getKey(), header.getValue());
        }

        if (userAgent != null) {
            boundRequestBuilder.setHeader(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }

        return boundRequestBuilder.execute(new OAuthAsyncCompletionHandler<>(callback, converter));
    }

    private static class ByteArrayConsumer implements Consumer<AsyncHttpClient.BoundRequestBuilder> {

        private final byte[] bodyContents;

        private ByteArrayConsumer(byte[] bodyContents) {
            this.bodyContents = bodyContents;
        }

        @Override
        public void accept(AsyncHttpClient.BoundRequestBuilder requestBuilder) {
            requestBuilder.setBody(bodyContents);
        }
    }

    private static class StringConsumer implements Consumer<AsyncHttpClient.BoundRequestBuilder> {

        private final String bodyContents;

        private StringConsumer(String bodyContents) {
            this.bodyContents = bodyContents;
        }

        @Override
        public void accept(AsyncHttpClient.BoundRequestBuilder requestBuilder) {
            requestBuilder.setBody(bodyContents);
        }
    }

    private static class FileConsumer implements Consumer<AsyncHttpClient.BoundRequestBuilder> {

        private final File bodyContents;

        private FileConsumer(File bodyContents) {
            this.bodyContents = bodyContents;
        }

        @Override
        public void accept(AsyncHttpClient.BoundRequestBuilder requestBuilder) {
            requestBuilder.setBody(bodyContents);
        }
    }
}
