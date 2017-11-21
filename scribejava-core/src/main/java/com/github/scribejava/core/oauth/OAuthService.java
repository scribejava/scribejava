package com.github.scribejava.core.oauth;

import com.github.scribejava.core.httpclient.HttpClientProvider;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.httpclient.jdk.JDKHttpClient;
import com.github.scribejava.core.httpclient.jdk.JDKHttpClientConfig;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import java.io.Closeable;
import java.io.File;

import java.io.IOException;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class OAuthService implements Closeable {

    private final OAuthConfig config;
    private final HttpClient httpClient;

    public OAuthService(OAuthConfig config) {
        this.config = config;
        final HttpClientConfig httpClientConfig = config.getHttpClientConfig();
        final HttpClient externalHttpClient = config.getHttpClient();

        if (httpClientConfig == null && externalHttpClient == null) {
            httpClient = new JDKHttpClient(JDKHttpClientConfig.defaultConfig());
        } else {
            httpClient = externalHttpClient == null ? getClient(httpClientConfig) : externalHttpClient;
        }
    }

    private static HttpClient getClient(HttpClientConfig config) {
        for (HttpClientProvider provider : ServiceLoader.load(HttpClientProvider.class)) {
            final HttpClient client = provider.createClient(config);
            if (client != null) {
                return client;
            }
        }
        return null;
    }

    @Override
    public void close() throws IOException {
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

    public Future<Response> executeAsync(OAuthRequest request) {
        return execute(request, null);
    }

    public Future<Response> execute(OAuthRequest request, OAuthAsyncRequestCallback<Response> callback) {
        return execute(request, callback, null);
    }

    public <R> Future<R> execute(OAuthRequest request, OAuthAsyncRequestCallback<R> callback,
            OAuthRequest.ResponseConverter<R> converter) {

        final File filePayload = request.getFilePayload();
        if (filePayload != null) {
            return httpClient.executeAsync(config.getUserAgent(), request.getHeaders(), request.getVerb(),
                    request.getCompleteUrl(), filePayload, callback, converter);
        } else if (request.getStringPayload() != null) {
            return httpClient.executeAsync(config.getUserAgent(), request.getHeaders(), request.getVerb(),
                    request.getCompleteUrl(), request.getStringPayload(), callback, converter);
        } else {
            return httpClient.executeAsync(config.getUserAgent(), request.getHeaders(), request.getVerb(),
                    request.getCompleteUrl(), request.getByteArrayPayload(), callback, converter);
        }
    }

    public Response execute(OAuthRequest request) throws InterruptedException, ExecutionException, IOException {
        final File filePayload = request.getFilePayload();
        if (filePayload != null) {
            return httpClient.execute(config.getUserAgent(), request.getHeaders(), request.getVerb(),
                    request.getCompleteUrl(), filePayload);
        } else if (request.getStringPayload() != null) {
            return httpClient.execute(config.getUserAgent(), request.getHeaders(), request.getVerb(),
                    request.getCompleteUrl(), request.getStringPayload());
        } else {
            return httpClient.execute(config.getUserAgent(), request.getHeaders(), request.getVerb(),
                    request.getCompleteUrl(), request.getByteArrayPayload());
        }
    }
}
