package com.github.scribejava.core.oauth;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.httpclient.HttpClientProvider;
import com.github.scribejava.core.model.ForceTypeOfHttpRequest;
import com.github.scribejava.core.model.HttpClient;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.ScribeJavaConfig;
import com.github.scribejava.core.model.Verb;

import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.Future;

/**
 * The main ScribeJava object.
 *
 * A facade responsible for the retrieval of request and access tokens and for the signing of HTTP requests.
 */
public abstract class OAuthService {

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

    public <T> Future<T> executeAsync(Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequestAsync.ResponseConverter<T> converter) {
        return httpClient.executeAsync(config.getUserAgent(), headers, httpVerb,
                    completeUrl, bodyContents, callback, converter);
    }
}
