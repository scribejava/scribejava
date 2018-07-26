package com.github.scribejava.httpclient.ahc;

import com.github.scribejava.core.httpclient.AbstractAsyncOnlyHttpClient;
import com.github.scribejava.core.httpclient.MultipartPayload;
import com.github.scribejava.core.java8.Consumer;
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
            final byte[] bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {
        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, new ByteArrayConsumer(bodyContents), callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            MultipartPayload bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {

        throw new UnsupportedOperationException("AhcHttpClient does not support MultipartPayload yet.");
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

        for (Map.Entry<String, String> header : headers.entrySet()) {
            boundRequestBuilder.addHeader(header.getKey(), header.getValue());
        }

        if (userAgent != null) {
            boundRequestBuilder.setHeader(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }

        return boundRequestBuilder.execute(new OAuthAsyncCompletionHandler<>(callback, converter));
    }

    private static class ByteArrayConsumer implements Consumer<BoundRequestBuilder> {

        private final byte[] bodyContents;

        private ByteArrayConsumer(byte[] bodyContents) {
            this.bodyContents = bodyContents;
        }

        @Override
        public void accept(BoundRequestBuilder requestBuilder) {
            requestBuilder.setBody(bodyContents);
        }
    }

    private static class StringConsumer implements Consumer<BoundRequestBuilder> {

        private final String bodyContents;

        private StringConsumer(String bodyContents) {
            this.bodyContents = bodyContents;
        }

        @Override
        public void accept(BoundRequestBuilder requestBuilder) {
            requestBuilder.setBody(bodyContents);
        }
    }

    private static class FileConsumer implements Consumer<BoundRequestBuilder> {

        private final File bodyContents;

        private FileConsumer(File bodyContents) {
            this.bodyContents = bodyContents;
        }

        @Override
        public void accept(BoundRequestBuilder requestBuilder) {
            requestBuilder.setBody(bodyContents);
        }
    }
}
