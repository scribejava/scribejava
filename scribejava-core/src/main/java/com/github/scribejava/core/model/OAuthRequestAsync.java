package com.github.scribejava.core.model;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import com.ning.http.client.ProxyServer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.oauth.OAuthService;

public class OAuthRequestAsync extends AbstractRequest {

    public static final ResponseConverter<Response> RESPONSE_CONVERTER = new ResponseConverter<Response>() {
        @Override
        public Response convert(final com.ning.http.client.Response response) throws IOException {
            final FluentCaseInsensitiveStringsMap map = response.getHeaders();
            final Map<String, String> headersMap = new HashMap<>();
            for (final FluentCaseInsensitiveStringsMap.Entry<String, List<String>> header : map) {
                final StringBuilder value = new StringBuilder();
                for (final String str : header.getValue()) {
                    value.append(str);
                }
                headersMap.put(header.getKey(), value.toString());
            }
            return new Response(response.getStatusCode(), response.getStatusText(), headersMap, response.getResponseBody(), response.
                    getResponseBodyAsStream());
        }
    };

    public OAuthRequestAsync(final Verb verb, final String url, final OAuthService service) {
        super(verb, url, service);
    }

    public <T> Future<T> sendAsync(final OAuthAsyncRequestCallback<T> callback, final ResponseConverter<T> converter) {
        return sendAsync(callback, converter, null);
    }

    public <T> Future<T> sendAsync(final OAuthAsyncRequestCallback<T> callback, final ResponseConverter<T> converter, final ProxyServer proxyServer) {
        final ForceTypeOfHttpRequest forceTypeOfHttpRequest = ScribeJavaConfig.getForceTypeOfHttpRequests();
        if (ForceTypeOfHttpRequest.FORCE_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
            throw new OAuthException("Cannot use async operations, only sync");
        }
        final OAuthService service = getService();
        if (ForceTypeOfHttpRequest.PREFER_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
            service.getConfig().log("Cannot use async operations, only sync");
        }
        final String completeUrl = getCompleteUrl();
        final AsyncHttpClient.BoundRequestBuilder boundRequestBuilder;
        switch (getVerb()) {
            case GET:
                boundRequestBuilder = service.getAsyncHttpClient().prepareGet(completeUrl);
                break;
            case POST:
                boundRequestBuilder = service.getAsyncHttpClient().preparePost(completeUrl).setBody(getBodyContents());
                break;
            default:
                throw new IllegalArgumentException("message build error: unknown verb type");
        }
        if (proxyServer != null) {
            boundRequestBuilder.setProxyServer(proxyServer);
        }
        return boundRequestBuilder.execute(new OAuthAsyncCompletionHandler<>(callback, converter));
    }

    private static class OAuthAsyncCompletionHandler<T> extends AsyncCompletionHandler<T> {

        private final OAuthAsyncRequestCallback<T> callback;
        private final ResponseConverter<T> converter;

        OAuthAsyncCompletionHandler(final OAuthAsyncRequestCallback<T> callback, final ResponseConverter<T> converter) {
            this.callback = callback;
            this.converter = converter;
        }

        @Override
        public T onCompleted(final com.ning.http.client.Response response) throws IOException {
            final T t = converter.convert(response);
            if (callback != null) {
                callback.onCompleted(t);
            }
            return t;
        }

        @Override
        public void onThrowable(final Throwable t) {
            if (callback != null) {
                callback.onThrowable(t);
            }
        }
    };

    public Future<Response> sendAsync(final OAuthAsyncRequestCallback<Response> callback) {
        return sendAsync(callback, RESPONSE_CONVERTER, null);
    }

    public Future<Response> sendAsync(final OAuthAsyncRequestCallback<Response> callback, final ProxyServer proxyServer) {
        return sendAsync(callback, RESPONSE_CONVERTER, proxyServer);
    }

    public interface ResponseConverter<T> {

        T convert(com.ning.http.client.Response response) throws IOException;
    }
}
