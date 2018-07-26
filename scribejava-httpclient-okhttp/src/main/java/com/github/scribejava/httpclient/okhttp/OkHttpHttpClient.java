package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.MultipartPayload;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
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
import com.github.scribejava.core.model.Response;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import okhttp3.Cache;
import okhttp3.Headers;
import okhttp3.ResponseBody;

public class OkHttpHttpClient implements HttpClient {

    private static final MediaType DEFAULT_CONTENT_TYPE_MEDIA_TYPE = MediaType.parse(DEFAULT_CONTENT_TYPE);

    private final OkHttpClient client;

    public OkHttpHttpClient() {
        this(OkHttpHttpClientConfig.defaultConfig());
    }

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
            byte[] bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodyType.BYTE_ARRAY, bodyContents, callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            MultipartPayload bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {

        throw new UnsupportedOperationException("OKHttpClient does not support Multipart payload for the moment");
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodyType.STRING, bodyContents, callback,
                converter);
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodyType.FILE, bodyContents, callback,
                converter);
    }

    private <T> Future<T> doExecuteAsync(String userAgent, Map<String, String> headers, Verb httpVerb,
            String completeUrl, BodyType bodyType, Object bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {
        final Call call = createCall(userAgent, headers, httpVerb, completeUrl, bodyType, bodyContents);
        final OkHttpFuture<T> okHttpFuture = new OkHttpFuture<>(call);
        call.enqueue(new OAuthAsyncCompletionHandler<>(callback, converter, okHttpFuture));
        return okHttpFuture;
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            byte[] bodyContents) throws InterruptedException, ExecutionException, IOException {

        return doExecute(userAgent, headers, httpVerb, completeUrl, BodyType.BYTE_ARRAY, bodyContents);
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            MultipartPayload bodyContents) throws InterruptedException, ExecutionException, IOException {

        throw new UnsupportedOperationException("OKHttpClient does not support Multipart payload for the moment");
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents) throws InterruptedException, ExecutionException, IOException {

        return doExecute(userAgent, headers, httpVerb, completeUrl, BodyType.STRING, bodyContents);
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents) throws InterruptedException, ExecutionException, IOException {

        return doExecute(userAgent, headers, httpVerb, completeUrl, BodyType.FILE, bodyContents);
    }

    private Response doExecute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            BodyType bodyType, Object bodyContents) throws IOException {
        final Call call = createCall(userAgent, headers, httpVerb, completeUrl, bodyType, bodyContents);
        return convertResponse(call.execute());
    }

    private Call createCall(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            BodyType bodyType, Object bodyContents) {
        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(completeUrl);

        final String method = httpVerb.name();

        // prepare body
        final RequestBody body;
        if (bodyContents != null && HttpMethod.permitsRequestBody(method)) {
            final MediaType mediaType = headers.containsKey(CONTENT_TYPE) ? MediaType.parse(headers.get(CONTENT_TYPE))
                    : DEFAULT_CONTENT_TYPE_MEDIA_TYPE;

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
        return client.newCall(requestBuilder.build());
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

    static Response convertResponse(okhttp3.Response okHttpResponse) {
        final Headers headers = okHttpResponse.headers();
        final Map<String, String> headersMap = new HashMap<>();
        for (String headerName : headers.names()) {
            headersMap.put(headerName, headers.get(headerName));
        }

        final ResponseBody body = okHttpResponse.body();
        return new Response(okHttpResponse.code(), okHttpResponse.message(), headersMap,
                body == null ? null : body.byteStream());
    }

}
