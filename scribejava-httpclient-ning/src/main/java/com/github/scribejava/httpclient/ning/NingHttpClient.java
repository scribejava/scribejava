package com.github.scribejava.httpclient.ning;

import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.HttpClient;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Verb;
import com.ning.http.client.AsyncHttpClient;

import java.util.Map;
import java.util.concurrent.Future;

import static com.github.scribejava.core.model.AbstractRequest.DEFAULT_CONTENT_TYPE;

public class NingHttpClient implements HttpClient {

    private final AsyncHttpClient client;

    public NingHttpClient(NingHttpClientConfig ningConfig) {
        final String ningAsyncHttpProviderClassName = ningConfig.getNingAsyncHttpProviderClassName();
        client = ningAsyncHttpProviderClassName == null
                ? new com.ning.http.client.AsyncHttpClient(ningConfig.getConfig())
                : new com.ning.http.client.AsyncHttpClient(ningAsyncHttpProviderClassName, ningConfig.getConfig());
    }

    @Override
    public void close() {
        client.close();
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
                                      String bodyContents, OAuthAsyncRequestCallback<T> callback,
                                      OAuthRequestAsync.ResponseConverter<T> converter) {
        final com.ning.http.client.AsyncHttpClient.BoundRequestBuilder boundRequestBuilder;
        switch (httpVerb) {
            case GET:
                boundRequestBuilder = client.prepareGet(completeUrl);
                break;
            case POST:
                com.ning.http.client.AsyncHttpClient.BoundRequestBuilder requestBuilder
                        = client.preparePost(completeUrl);
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
                .execute(new OAuthAsyncCompletionHandler<>(
                        callback, converter));
    }
}
