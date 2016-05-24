package com.github.scribejava.core.model;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.oauth.OAuthService;

import io.netty.handler.codec.http.HttpHeaders;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.proxy.ProxyServer;

public class OAuthRequestAsync extends AbstractRequest {

    public static final ResponseConverter<Response> RESPONSE_CONVERTER = new ResponseConverter<Response>() {
        @Override
        public Response convert(org.asynchttpclient.Response response) throws IOException {
            final HttpHeaders map = response.getHeaders();
            final Map<String, String> headersMap = new HashMap<>();
            for (Entry<String, String> header : map) {
                headersMap.put(header.getKey(), header.getValue());
            }
            return new Response(response.getStatusCode(), response.getStatusText(), headersMap,
                    response.getResponseBody(), response.getResponseBodyAsStream());
        }
    };

    public OAuthRequestAsync(Verb verb, String url, OAuthService service) {
        super(verb, url, service);
    }

    public <T> Future<T> sendAsync(OAuthAsyncRequestCallback<T> callback, ResponseConverter<T> converter) {
        return sendAsync(callback, converter, null);
    }

    public <T> Future<T> sendAsync(OAuthAsyncRequestCallback<T> callback, ResponseConverter<T> converter,
            ProxyServer proxyServer) {
        final ForceTypeOfHttpRequest forceTypeOfHttpRequest = ScribeJavaConfig.getForceTypeOfHttpRequests();
        if (ForceTypeOfHttpRequest.FORCE_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
            throw new OAuthException("Cannot use async operations, only sync");
        }
        final OAuthService service = getService();
        final OAuthConfig config = service.getConfig();
        if (ForceTypeOfHttpRequest.PREFER_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
            config.log("Cannot use async operations, only sync");
        }
        final String completeUrl = getCompleteUrl();
        final BoundRequestBuilder boundRequestBuilder;
        final AsyncHttpClient asyncHttpClient = service.getAsyncHttpClient();
        final Map<String, String> headers = getHeaders();
        switch (getVerb()) {
            case GET:
                boundRequestBuilder = asyncHttpClient.prepareGet(completeUrl);
                break;
            case POST:
                BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(completeUrl);
                if (!headers.containsKey(CONTENT_TYPE)) {
                    requestBuilder = requestBuilder.addHeader(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
                }
                boundRequestBuilder = requestBuilder.setBody(getBodyContents());
                break;
            default:
                throw new IllegalArgumentException("message build error: unknown verb type");
        }

        for (Map.Entry<String, String> header : headers.entrySet()) {
            boundRequestBuilder.addHeader(header.getKey(), header.getValue());
        }
        final String userAgent = config.getUserAgent();
        if (userAgent != null) {
            boundRequestBuilder.setHeader(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }

        if (proxyServer != null) {
            boundRequestBuilder.setProxyServer(proxyServer);
        }
        return boundRequestBuilder.execute(new OAuthAsyncCompletionHandler<>(callback, converter));
    }

    private static class OAuthAsyncCompletionHandler<T> extends AsyncCompletionHandler<T> {

        private final OAuthAsyncRequestCallback<T> callback;
        private final ResponseConverter<T> converter;

        OAuthAsyncCompletionHandler(OAuthAsyncRequestCallback<T> callback, ResponseConverter<T> converter) {
            this.callback = callback;
            this.converter = converter;
        }

        @Override
        public T onCompleted(org.asynchttpclient.Response response) throws IOException {
            final T t = converter.convert(response);
            if (callback != null) {
                callback.onCompleted(t);
            }
            return t;
        }

        @Override
        public void onThrowable(Throwable t) {
            if (callback != null) {
                callback.onThrowable(t);
            }
        }
    };

    public Future<Response> sendAsync(OAuthAsyncRequestCallback<Response> callback) {
        return sendAsync(callback, RESPONSE_CONVERTER, null);
    }

    public Future<Response> sendAsync(OAuthAsyncRequestCallback<Response> callback, ProxyServer proxyServer) {
        return sendAsync(callback, RESPONSE_CONVERTER, proxyServer);
    }

    public interface ResponseConverter<T> {

        T convert(org.asynchttpclient.Response response) throws IOException;
    }
}
