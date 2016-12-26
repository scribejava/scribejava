package com.github.scribejava.httpclient.ahc;

import com.github.scribejava.core.httpclient.AbstractAsyncOnlyHttpClient;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Verb;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

import static com.github.scribejava.core.model.AbstractRequest.DEFAULT_CONTENT_TYPE;
import java.io.File;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.BoundRequestBuilder;

public class AhcHttpClient extends AbstractAsyncOnlyHttpClient {

    private final AsyncHttpClient client;

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
        BoundRequestBuilder boundRequestBuilder;
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
            BoundRequestBuilder setBody(BoundRequestBuilder requestBuilder, Object bodyContents) {
                return requestBuilder.setBody((byte[]) bodyContents);
            }
        },
        STRING {
            @Override
            BoundRequestBuilder setBody(BoundRequestBuilder requestBuilder, Object bodyContents) {
                return requestBuilder.setBody((String) bodyContents);
            }
        },
        FILE {
            @Override
            BoundRequestBuilder setBody(BoundRequestBuilder requestBuilder, Object bodyContents) {
                return requestBuilder.setBody((File) bodyContents);
            }
        };

        abstract BoundRequestBuilder setBody(BoundRequestBuilder requestBuilder, Object bodyContents);
    }
}
