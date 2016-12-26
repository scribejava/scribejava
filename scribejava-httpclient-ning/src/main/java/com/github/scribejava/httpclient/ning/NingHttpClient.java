package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.httpclient.AbstractAsyncOnlyHttpClient;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Verb;
import com.ning.http.client.AsyncHttpClient;

import java.util.Map;
import java.util.concurrent.Future;

import static com.github.scribejava.core.model.AbstractRequest.DEFAULT_CONTENT_TYPE;
import com.ning.http.client.AsyncHttpClientConfig;
import java.io.File;

public class NingHttpClient extends AbstractAsyncOnlyHttpClient {

    private final AsyncHttpClient client;

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
            byte[] bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodySetter.BYTE_ARRAY, bodyContents, callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodySetter.STRING, bodyContents, callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodySetter.FILE, bodyContents, callback,
                converter);
    }

    private <T> Future<T> doExecuteAsync(String userAgent, Map<String, String> headers, Verb httpVerb,
            String completeUrl, BodySetter bodySetter, Object bodyContents, OAuthAsyncRequestCallback<T> callback,
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
            boundRequestBuilder = bodySetter.setBody(boundRequestBuilder, bodyContents);
        }

        for (Map.Entry<String, String> header : headers.entrySet()) {
            boundRequestBuilder.addHeader(header.getKey(), header.getValue());
        }
        if (userAgent != null) {
            boundRequestBuilder.setHeader(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }

        return boundRequestBuilder.execute(new OAuthAsyncCompletionHandler<>(callback, converter));
    }

    private enum BodySetter {
        BYTE_ARRAY {
            @Override
            AsyncHttpClient.BoundRequestBuilder setBody(AsyncHttpClient.BoundRequestBuilder requestBuilder,
                    Object bodyContents) {
                return requestBuilder.setBody((byte[]) bodyContents);
            }
        },
        STRING {
            @Override
            AsyncHttpClient.BoundRequestBuilder setBody(AsyncHttpClient.BoundRequestBuilder requestBuilder,
                    Object bodyContents) {
                return requestBuilder.setBody((String) bodyContents);
            }
        },
        FILE {
            @Override
            AsyncHttpClient.BoundRequestBuilder setBody(AsyncHttpClient.BoundRequestBuilder requestBuilder,
                    Object bodyContents) {
                return requestBuilder.setBody((File) bodyContents);
            }
        };

        abstract AsyncHttpClient.BoundRequestBuilder setBody(AsyncHttpClient.BoundRequestBuilder requestBuilder,
                Object bodyContents);
    }
}
