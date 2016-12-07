package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.HttpClient;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Verb;
import com.ning.http.client.AsyncHttpClient;

import java.util.Map;
import java.util.concurrent.Future;

import static com.github.scribejava.core.model.AbstractRequest.DEFAULT_CONTENT_TYPE;
import java.io.File;

public class NingHttpClient implements HttpClient {

    private final AsyncHttpClient client;

    public NingHttpClient(NingHttpClientConfig ningConfig) {
        final String ningAsyncHttpProviderClassName = ningConfig.getNingAsyncHttpProviderClassName();
        client = ningAsyncHttpProviderClassName == null ? new AsyncHttpClient(ningConfig.getConfig())
                : new AsyncHttpClient(ningAsyncHttpProviderClassName, ningConfig.getConfig());
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
            byte[] bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, new ByteArrayBodySetter(bodyContents),
                callback, converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, new StringBodySetter(bodyContents), callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, new FileBodySetter(bodyContents), callback,
                converter);
    }

    private <T> Future<T> doExecuteAsync(String userAgent, Map<String, String> headers, Verb httpVerb,
            String completeUrl, BodySetter bodySetter, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {
        AsyncHttpClient.BoundRequestBuilder boundRequestBuilder;
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

        if (httpVerb == Verb.POST || httpVerb == Verb.PUT || httpVerb == Verb.DELETE) {
            if (!headers.containsKey(AbstractRequest.CONTENT_TYPE)) {
                boundRequestBuilder = boundRequestBuilder.addHeader(AbstractRequest.CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
            }
            boundRequestBuilder = bodySetter.setBody(boundRequestBuilder);
        }

        for (Map.Entry<String, String> header : headers.entrySet()) {
            boundRequestBuilder.addHeader(header.getKey(), header.getValue());
        }
        if (userAgent != null) {
            boundRequestBuilder.setHeader(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }

        return boundRequestBuilder
                .execute(new OAuthAsyncCompletionHandler<>(
                        callback, converter));
    }

    private interface BodySetter {

        AsyncHttpClient.BoundRequestBuilder setBody(AsyncHttpClient.BoundRequestBuilder requestBuilder);
    }

    private static class ByteArrayBodySetter implements BodySetter {

        private final byte[] bodyContents;

        private ByteArrayBodySetter(byte[] bodyContents) {
            this.bodyContents = bodyContents;
        }

        @Override
        public AsyncHttpClient.BoundRequestBuilder setBody(AsyncHttpClient.BoundRequestBuilder requestBuilder) {
            return requestBuilder.setBody(bodyContents);
        }
    }

    private static class StringBodySetter implements BodySetter {

        private final String bodyContents;

        private StringBodySetter(String bodyContents) {
            this.bodyContents = bodyContents;
        }

        @Override
        public AsyncHttpClient.BoundRequestBuilder setBody(AsyncHttpClient.BoundRequestBuilder requestBuilder) {
            return requestBuilder.setBody(bodyContents);
        }
    }

    private static class FileBodySetter implements BodySetter {

        private final File bodyContents;

        private FileBodySetter(File bodyContents) {
            this.bodyContents = bodyContents;
        }

        @Override
        public AsyncHttpClient.BoundRequestBuilder setBody(AsyncHttpClient.BoundRequestBuilder requestBuilder) {
            return requestBuilder.setBody(bodyContents);
        }
    }
}
