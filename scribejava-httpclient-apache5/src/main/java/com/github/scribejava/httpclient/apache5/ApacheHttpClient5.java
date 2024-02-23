package com.github.scribejava.httpclient.apache5;

import com.github.scribejava.core.httpclient.AbstractAsyncOnlyHttpClient;
import com.github.scribejava.core.httpclient.multipart.MultipartPayload;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.core5.http.nio.AsyncEntityProducer;
import org.apache.hc.core5.http.nio.entity.BasicAsyncEntityProducer;
import org.apache.hc.core5.http.nio.entity.FileEntityProducer;
import org.apache.hc.core5.http.nio.entity.StringAsyncEntityProducer;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApacheHttpClient5 extends AbstractAsyncOnlyHttpClient {

    private static final int ENTITY_CONSUMER_BUFFER_SIZE = 4096;
    private static final int ENTITY_CONSUMER_THREADS = 5;

    private final CloseableHttpAsyncClient client;
    private final ExecutorService entityConsumerExecutor;

    public ApacheHttpClient5() {
        this(ApacheHttpClient5Config.defaultConfig());
    }

    public ApacheHttpClient5(ApacheHttpClient5Config config) {
        this(config.getHttpAsyncClientBuilder());
    }

    public ApacheHttpClient5(HttpAsyncClientBuilder builder) {
        this(builder.build());
    }

    public ApacheHttpClient5(CloseableHttpAsyncClient client) {
        this.client = client;
        this.client.start();

        entityConsumerExecutor = Executors.newFixedThreadPool(ENTITY_CONSUMER_THREADS);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            byte[] bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {
        final AsyncEntityProducer entity = bodyContents == null ? null : new BasicAsyncEntityProducer(bodyContents);
        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, entity, callback, converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            MultipartPayload bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {

        throw new UnsupportedOperationException("ApacheHttpClient does not support MultipartPayload yet.");
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {
        final AsyncEntityProducer entity = bodyContents == null ? null : new StringAsyncEntityProducer(bodyContents);
        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, entity, callback, converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {
        final AsyncEntityProducer entity = bodyContents == null ? null : new FileEntityProducer(bodyContents);
        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, entity, callback, converter);
    }

    private <T> Future<T> doExecuteAsync(String userAgent, Map<String, String> headers, Verb httpVerb,
            String completeUrl, AsyncEntityProducer entityProducer, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {
        final AsyncRequestBuilder builder = getRequestBuilder(httpVerb);
        builder.setUri(completeUrl);

        if (httpVerb.isPermitBody()) {
            if (!headers.containsKey(CONTENT_TYPE)) {
                builder.addHeader(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
            }
            builder.setEntity(entityProducer);
        }

        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }

        if (userAgent != null) {
            builder.setHeader(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }

        final AsyncHttpEntityConsumer entityConsumer = new AsyncHttpEntityConsumer(
                ENTITY_CONSUMER_BUFFER_SIZE, entityConsumerExecutor);
        final ResponseWithEntityConsumer responseConsumer = new ResponseWithEntityConsumer(entityConsumer);
        final OAuthAsyncCompletionHandler<T> handler = new OAuthAsyncCompletionHandler<>(callback, converter);
        final Future<ResponseWithEntity> future = client.execute(builder.build(), responseConsumer, handler);
        return new ApacheHttpFuture<>(future, handler);
    }

    private static AsyncRequestBuilder getRequestBuilder(Verb httpVerb) {
        switch (httpVerb) {
            case GET:
                return AsyncRequestBuilder.get();
            case PUT:
                return AsyncRequestBuilder.put();
            case DELETE:
                return AsyncRequestBuilder.delete();
            case HEAD:
                return AsyncRequestBuilder.head();
            case POST:
                return AsyncRequestBuilder.post();
            case PATCH:
                return AsyncRequestBuilder.patch();
            case TRACE:
                return AsyncRequestBuilder.trace();
            case OPTIONS:
                return AsyncRequestBuilder.options();
            default:
                throw new IllegalArgumentException("message build error: unknown verb type");
        }
    }
}
