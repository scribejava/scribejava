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
                final String ningAsyncHttpProviderClassName = config.getNingAsyncHttpProviderClassName();
                ningAsyncHttpClient = ningAsyncHttpProviderClassName == null
                        ? new com.ning.http.client.AsyncHttpClient(ningConfig)
                        : new com.ning.http.client.AsyncHttpClient(ningAsyncHttpProviderClassName, ningConfig);
                ahcAsyncHttpClient = null;
            } else {
                ahcAsyncHttpClient = new org.asynchttpclient.DefaultAsyncHttpClient(ahcConfig);
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
            return ningExecuteAsync(headers, httpVerb, completeUrl, bodyContents, callback, converter);
        } else {
            return ahcExecuteAsync(headers, httpVerb, completeUrl, bodyContents, callback, converter);
        }
    }

    private <T> Future<T> ningExecuteAsync(Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {
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
        final String userAgent = config.getUserAgent();
        if (userAgent != null) {
            boundRequestBuilder.setHeader(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }

        return boundRequestBuilder
                .execute(new com.github.scribejava.core.async.ning.OAuthAsyncCompletionHandler<>(callback, converter));
    }

    private <T> Future<T> ahcExecuteAsync(Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {
        final org.asynchttpclient.BoundRequestBuilder boundRequestBuilder;
        switch (httpVerb) {
            case GET:
                boundRequestBuilder = ahcAsyncHttpClient.prepareGet(completeUrl);
                break;
            case POST:
                org.asynchttpclient.BoundRequestBuilder requestBuilder = ahcAsyncHttpClient.preparePost(completeUrl);
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
        final String userAgent = config.getUserAgent();
        if (userAgent != null) {
            boundRequestBuilder.setHeader(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }

        return boundRequestBuilder
                .execute(new com.github.scribejava.core.async.ahc.OAuthAsyncCompletionHandler<>(callback, converter));
    }
}
