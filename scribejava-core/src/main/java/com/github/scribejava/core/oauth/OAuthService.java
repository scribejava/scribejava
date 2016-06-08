package com.github.scribejava.core.oauth;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.AbstractRequest;
import static com.github.scribejava.core.model.AbstractRequest.DEFAULT_CONTENT_TYPE;
import com.github.scribejava.core.model.ForceTypeOfHttpRequest;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.ScribeJavaConfig;
import com.github.scribejava.core.model.Verb;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * The main ScribeJava object.
 *
 * A facade responsible for the retrieval of request and access tokens and for the signing of HTTP requests.
 */
public abstract class OAuthService {

    private final OAuthConfig config;
    private final com.ning.http.client.AsyncHttpClient ningAsyncHttpClient;
    private final org.asynchttpclient.AsyncHttpClient ahcAsyncHttpClient;

    public OAuthService(OAuthConfig config) {
        this.config = config;
        final ForceTypeOfHttpRequest forceTypeOfHttpRequest = ScribeJavaConfig.getForceTypeOfHttpRequests();
        final com.ning.http.client.AsyncHttpClientConfig ningConfig = config.getNingAsyncHttpClientConfig();
        final org.asynchttpclient.AsyncHttpClientConfig ahcConfig = config.getAhcAsyncHttpClientConfig();

        if (ningConfig == null && ahcConfig == null) {
            if (ForceTypeOfHttpRequest.FORCE_ASYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
                throw new OAuthException("Cannot use sync operations, only async");
            }
            if (ForceTypeOfHttpRequest.PREFER_ASYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
                config.log("Cannot use sync operations, only async");
            }
            ningAsyncHttpClient = null;
            ahcAsyncHttpClient = null;
        } else {
            if (ForceTypeOfHttpRequest.FORCE_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
                throw new OAuthException("Cannot use async operations, only sync");
            }
            if (ForceTypeOfHttpRequest.PREFER_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
                config.log("Cannot use async operations, only sync");
            }

            if (ahcConfig == null) {
                ningAsyncHttpClient = NingProvider.createClient(config.getNingAsyncHttpProviderClassName(), ningConfig);
                ahcAsyncHttpClient = null;
            } else {
                ahcAsyncHttpClient = AHCProvider.createClient(ahcConfig);
                ningAsyncHttpClient = null;
            }
        }
    }

    public void closeAsyncClient() throws IOException {
        if (ahcAsyncHttpClient == null) {
            ningAsyncHttpClient.close();
        } else {
            ahcAsyncHttpClient.close();
        }
    }

    public OAuthConfig getConfig() {
        return config;
    }

    /**
     * Returns the OAuth version of the service.
     *
     * @return OAuth version as string
     */
    public abstract String getVersion();

    public <T> Future<T> executeAsync(Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {
        if (ahcAsyncHttpClient == null) {
            return NingProvider.ningExecuteAsync(ningAsyncHttpClient, config.getUserAgent(), headers, httpVerb,
                    completeUrl, bodyContents, callback, converter);
        } else {
            return AHCProvider.ahcExecuteAsync(ahcAsyncHttpClient, config.getUserAgent(), headers, httpVerb,
                    completeUrl, bodyContents, callback, converter);
        }
    }

    private static class NingProvider {

        private static com.ning.http.client.AsyncHttpClient createClient(String ningAsyncHttpProviderClassName,
                com.ning.http.client.AsyncHttpClientConfig ningConfig) {
            return ningAsyncHttpProviderClassName == null
                    ? new com.ning.http.client.AsyncHttpClient(ningConfig)
                    : new com.ning.http.client.AsyncHttpClient(ningAsyncHttpProviderClassName, ningConfig);
        }

        private static <T> Future<T> ningExecuteAsync(com.ning.http.client.AsyncHttpClient ningAsyncHttpClient,
                String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl, String bodyContents,
                OAuthAsyncRequestCallback<T> callback, OAuthRequestAsync.ResponseConverter<T> converter) {
            final com.ning.http.client.AsyncHttpClient.BoundRequestBuilder boundRequestBuilder;
            switch (httpVerb) {
                case GET:
                    boundRequestBuilder = ningAsyncHttpClient.prepareGet(completeUrl);
                    break;
                case POST:
                    com.ning.http.client.AsyncHttpClient.BoundRequestBuilder requestBuilder
                            = ningAsyncHttpClient.preparePost(completeUrl);
                    if (!headers.containsKey(AbstractRequest.CONTENT_TYPE)) {
                        requestBuilder = requestBuilder.addHeader(AbstractRequest.CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
                    }
                    boundRequestBuilder = requestBuilder.setBody(bodyContents);
                    break;
                default:
                    throw new IllegalArgumentException("message build error: unknown verb type");
            }

            for (Map.Entry<String, String> header : headers.entrySet()) {
                boundRequestBuilder.addHeader(header.getKey(), header.getValue());
            }
            if (userAgent != null) {
                boundRequestBuilder.setHeader(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
            }

            return boundRequestBuilder
                    .execute(new com.github.scribejava.core.async.ning.OAuthAsyncCompletionHandler<>(
                            callback, converter));
        }
    }

    private static class AHCProvider {

        private static org.asynchttpclient.AsyncHttpClient createClient(
                org.asynchttpclient.AsyncHttpClientConfig ahcConfig) {
            return new org.asynchttpclient.DefaultAsyncHttpClient(ahcConfig);
        }

        private static <T> Future<T> ahcExecuteAsync(org.asynchttpclient.AsyncHttpClient ahcAsyncHttpClient,
                String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl, String bodyContents,
                OAuthAsyncRequestCallback<T> callback, OAuthRequestAsync.ResponseConverter<T> converter) {
            final org.asynchttpclient.BoundRequestBuilder boundRequestBuilder;
            switch (httpVerb) {
                case GET:
                    boundRequestBuilder = ahcAsyncHttpClient.prepareGet(completeUrl);
                    break;
                case POST:
                    org.asynchttpclient.BoundRequestBuilder requestBuilder
                            = ahcAsyncHttpClient.preparePost(completeUrl);
                    if (!headers.containsKey(AbstractRequest.CONTENT_TYPE)) {
                        requestBuilder = requestBuilder.addHeader(AbstractRequest.CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
                    }
                    boundRequestBuilder = requestBuilder.setBody(bodyContents);
                    break;
                default:
                    throw new IllegalArgumentException("message build error: unknown verb type");
            }

            for (Map.Entry<String, String> header : headers.entrySet()) {
                boundRequestBuilder.addHeader(header.getKey(), header.getValue());
            }
            if (userAgent != null) {
                boundRequestBuilder.setHeader(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
            }

            return boundRequestBuilder
                    .execute(new com.github.scribejava.core.async.ahc.OAuthAsyncCompletionHandler<>(
                            callback, converter));
        }
    }
}
