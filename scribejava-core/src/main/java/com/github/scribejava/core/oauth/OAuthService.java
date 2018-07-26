package com.github.scribejava.core.oauth;

import com.github.scribejava.core.httpclient.HttpClientProvider;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.httpclient.jdk.JDKHttpClient;
import com.github.scribejava.core.httpclient.jdk.JDKHttpClientConfig;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import java.io.Closeable;
import java.io.File;

import java.io.IOException;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class OAuthService implements Closeable {

    private final String apiKey;
    private final String apiSecret;
    private final String callback;
    private final String scope;
    private final String userAgent;
    private final HttpClient httpClient;

    public OAuthService(String apiKey, String apiSecret, String callback, String scope, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.callback = callback;
        this.scope = scope;
        this.userAgent = userAgent;

        if (httpClientConfig == null && httpClient == null) {
            this.httpClient = new JDKHttpClient(JDKHttpClientConfig.defaultConfig());
        } else {
            this.httpClient = httpClient == null ? getClient(httpClientConfig) : httpClient;
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

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getCallback() {
        return callback;
    }

    public String getScope() {
        return scope;
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
            return httpClient.executeAsync(userAgent, request.getHeaders(), request.getVerb(), request.getCompleteUrl(),
                    filePayload, callback, converter);
        } else if (request.getStringPayload() != null) {
            return httpClient.executeAsync(userAgent, request.getHeaders(), request.getVerb(), request.getCompleteUrl(),
                    request.getStringPayload(), callback, converter);
        } else {
            return httpClient.executeAsync(userAgent, request.getHeaders(), request.getVerb(), request.getCompleteUrl(),
                    request.getByteArrayPayload(), callback, converter);
        }
    }

    public Response execute(OAuthRequest request) throws InterruptedException, ExecutionException, IOException {
        final File filePayload = request.getFilePayload();
        if (filePayload != null) {
            return httpClient.execute(userAgent, request.getHeaders(), request.getVerb(), request.getCompleteUrl(),
                    filePayload);
        } else if (request.getStringPayload() != null) {
            return httpClient.execute(userAgent, request.getHeaders(), request.getVerb(), request.getCompleteUrl(),
                    request.getStringPayload());
        } else if (request.getMultipartPayloads() != null) {
            return httpClient.execute(userAgent, request.getHeaders(), request.getVerb(), request.getCompleteUrl(),
                    request.getMultipartPayloads());
        } else {
            return httpClient.execute(userAgent, request.getHeaders(), request.getVerb(), request.getCompleteUrl(),
                    request.getByteArrayPayload());
        }
    }
}
