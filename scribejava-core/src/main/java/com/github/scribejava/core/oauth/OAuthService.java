package com.github.scribejava.core.oauth;

import com.github.scribejava.core.httpclient.HttpClientProvider;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import java.io.File;

import java.io.IOException;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutionException;
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
        final HttpClientConfig httpClientConfig = config.getHttpClientConfig();
        final HttpClient externalHttpClient = config.getHttpClient();

        if (httpClientConfig == null && externalHttpClient == null) {
            httpClient = null;
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

    public <R> Future<R> execute(OAuthRequestAsync request, OAuthAsyncRequestCallback<R> callback,
            OAuthRequestAsync.ResponseConverter<R> converter) {

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

    public Future<Response> execute(OAuthRequestAsync request, OAuthAsyncRequestCallback<Response> callback) {
        return execute(request, callback, null);
    }

    public Response execute(OAuthRequestAsync request) throws InterruptedException, ExecutionException, IOException {
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
