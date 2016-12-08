package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Verb;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

import static com.github.scribejava.core.model.AbstractRequest.DEFAULT_CONTENT_TYPE;
import java.io.File;
import okhttp3.Cache;

public class OkHttpHttpClient implements HttpClient {

    private static final MediaType DEFAULT_CONTENT_TYPE_MEDIA_TYPE = MediaType.parse(DEFAULT_CONTENT_TYPE);

    private final OkHttpClient client;

    public OkHttpHttpClient(OkHttpHttpClientConfig config) {
        final OkHttpClient.Builder clientBuilder = config.getClientBuilder();
        client = clientBuilder == null ? new OkHttpClient() : clientBuilder.build();
    }

    public OkHttpHttpClient(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public void close() throws IOException {
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        final Cache cache = client.cache();
        if (cache != null) {
            cache.close();
        }
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            byte[] bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {
        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodyType.BYTE_ARRAY, bodyContents, callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {
        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodyType.STRING, bodyContents, callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {
        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodyType.FILE, bodyContents, callback,
                converter);
    }

    private <T> Future<T> doExecuteAsync(String userAgent, Map<String, String> headers, Verb httpVerb,
            String completeUrl, BodyType bodyType, Object bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {
        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(completeUrl);

        final String method = httpVerb.name();

        // prepare body
        final RequestBody body;
        if (bodyContents != null && HttpMethod.permitsRequestBody(method)) {
            final MediaType mediaType = headers.containsKey(AbstractRequest.CONTENT_TYPE)
                    ? MediaType.parse(headers.get(AbstractRequest.CONTENT_TYPE)) : DEFAULT_CONTENT_TYPE_MEDIA_TYPE;

            body = bodyType.createBody(mediaType, bodyContents);
        } else {
            body = null;
        }

        // fill HTTP method and body
        requestBuilder.method(method, body);

        // fill headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }
        if (userAgent != null) {
            requestBuilder.header(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }

        // create a new call
        final Call call = client.newCall(requestBuilder.build());
        final OkHttpFuture<T> okHttpFuture = new OkHttpFuture<>(call);
        call.enqueue(new OAuthAsyncCompletionHandler<>(callback, converter, okHttpFuture));
        return okHttpFuture;
    }

    private enum BodyType {
        BYTE_ARRAY {
            @Override
            RequestBody createBody(MediaType mediaType, Object bodyContents) {
                return RequestBody.create(mediaType, (byte[]) bodyContents);
            }
        },
        STRING {
            @Override
            RequestBody createBody(MediaType mediaType, Object bodyContents) {
                return RequestBody.create(mediaType, (String) bodyContents);
            }
        },
        FILE {
            @Override
            RequestBody createBody(MediaType mediaType, Object bodyContents) {
                return RequestBody.create(mediaType, (File) bodyContents);
            }
        };

        abstract RequestBody createBody(MediaType mediaType, Object bodyContents);
    }
}
