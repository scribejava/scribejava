package com.github.scribejava.httpclient.ahc;

import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.HttpClient;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Verb;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

import static com.github.scribejava.core.model.AbstractRequest.DEFAULT_CONTENT_TYPE;
import org.asynchttpclient.BoundRequestBuilder;

public class AhcHttpClient implements HttpClient {

    private final AsyncHttpClient client;

    public AhcHttpClient(AhcHttpClientConfig ahcConfig) {
        client = new DefaultAsyncHttpClient(ahcConfig.getClientConfig());
    }

    public AhcHttpClient(DefaultAsyncHttpClient ahcClient) {
        client = ahcClient;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
                                      String bodyContents, OAuthAsyncRequestCallback<T> callback,
                                      OAuthRequestAsync.ResponseConverter<T> converter) {
        final BoundRequestBuilder boundRequestBuilder;
        switch (httpVerb) {
            case GET:
                boundRequestBuilder = client.prepareGet(completeUrl);
                break;
            case POST:
                BoundRequestBuilder requestBuilder = client.preparePost(completeUrl);
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
