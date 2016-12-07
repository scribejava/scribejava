package com.github.scribejava.core.oauth;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.httpclient.HttpClientProvider;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.ForceTypeOfHttpRequest;
import com.github.scribejava.core.model.HttpClient;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.ScribeJavaConfig;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;

import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.Future;

/**
 * The main ScribeJava object.
 *
 * A facade responsible for the retrieval of request and access tokens and for the signing of HTTP requests.
 * @param <T> type of token used to sign the request
 */
public abstract class OAuthService<T extends Token> {

    private final OAuthConfig config;
    private final HttpClient httpClient;

    public OAuthService(OAuthConfig config) {
        this.config = config;
        final ForceTypeOfHttpRequest forceTypeOfHttpRequest = ScribeJavaConfig.getForceTypeOfHttpRequests();
        final HttpClient.Config httpClientConfig = config.getHttpClientConfig();
        final HttpClient externalHttpClient = config.getHttpClient();

        if (httpClientConfig == null && externalHttpClient == null) {
            if (ForceTypeOfHttpRequest.FORCE_ASYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
                throw new OAuthException("Cannot use sync operations, only async");
            }
            if (ForceTypeOfHttpRequest.PREFER_ASYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
                config.log("Cannot use sync operations, only async");
            }
            httpClient = null;
        } else {
            if (ForceTypeOfHttpRequest.FORCE_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
                throw new OAuthException("Cannot use async operations, only sync");
            }
            if (ForceTypeOfHttpRequest.PREFER_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
                config.log("Cannot use async operations, only sync");
            }

            httpClient = externalHttpClient == null ? getClient(httpClientConfig) : externalHttpClient;
        }
    }

    private static HttpClient getClient(HttpClient.Config config) {
        for (HttpClientProvider provider : ServiceLoader.load(HttpClientProvider.class)) {
            final HttpClient client = provider.createClient(config);
            if (client != null) {
                return client;
            }
        }
        return null;
    }

    public void closeAsyncClient() throws IOException {
        httpClient.close();
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

    public abstract void signRequest(T token, AbstractRequest request);

    /**
     *
     * @param <T> T
     * @param headers headers
     * @param httpVerb httpVerb
     * @param completeUrl completeUrl
     * @param bodyContents bodyContents
     * @param callback callback
     * @param converter converter
     * @return Future
     * @deprecated use {@link #execute(com.github.scribejava.core.model.OAuthRequestAsync,
     * com.github.scribejava.core.model.OAuthAsyncRequestCallback,
     * com.github.scribejava.core.model.OAuthRequestAsync.ResponseConverter)}<br>
     * or<br>{@link #execute(com.github.scribejava.core.model.OAuthRequestAsync,
     * com.github.scribejava.core.model.OAuthAsyncRequestCallback)}
     */
    @Deprecated
    public <T> Future<T> executeAsync(Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {

        final ForceTypeOfHttpRequest forceTypeOfHttpRequest = ScribeJavaConfig.getForceTypeOfHttpRequests();
        if (ForceTypeOfHttpRequest.FORCE_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
            throw new OAuthException("Cannot use async operations, only sync");
        }
        if (ForceTypeOfHttpRequest.PREFER_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
            config.log("Cannot use async operations, only sync");
        }

        return httpClient.executeAsync(config.getUserAgent(), headers, httpVerb, completeUrl, bodyContents, callback,
                converter);
    }

    public <T> Future<T> execute(OAuthRequestAsync request, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {

        final ForceTypeOfHttpRequest forceTypeOfHttpRequest = ScribeJavaConfig.getForceTypeOfHttpRequests();
        if (ForceTypeOfHttpRequest.FORCE_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
            throw new OAuthException("Cannot use async operations, only sync");
        }
        if (ForceTypeOfHttpRequest.PREFER_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
            config.log("Cannot use async operations, only sync");
        }

        return httpClient.executeAsync(config.getUserAgent(), request.getHeaders(), request.getVerb(),
                request.getCompleteUrl(), request.getBodyContents(), callback, converter);
    }

    public Future<Response> execute(OAuthRequestAsync request, OAuthAsyncRequestCallback<Response> callback) {
        return execute(request, callback, null);
    }

    /**
     * the same as {@link OAuthRequest#send()}
     *
     * @param request request
     * @return Response
     */
    public Response execute(OAuthRequest request) {
        return request.send();
    }
}
