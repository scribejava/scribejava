package com.github.scribejava.httpclient.okhttp;

import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.HttpClient;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Verb;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

import static com.github.scribejava.core.model.AbstractRequest.DEFAULT_CONTENT_TYPE;

public class OkHttpHttpClient implements HttpClient {

    private final OkHttpClient client;

    public OkHttpHttpClient(OkHttpHttpClientConfig config) {
        client = config.getClient();
    }

    public OkHttpHttpClient(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public void close() throws IOException {
        //client.close();
    }

    @Override
    public <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
                                      String bodyContents, OAuthAsyncRequestCallback<T> callback,
                                      OAuthRequestAsync.ResponseConverter<T> converter) {
        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(completeUrl);

        switch (httpVerb) {
            case GET:
                requestBuilder.get();
                break;
            case POST:
                String contentType = headers.containsKey(AbstractRequest.CONTENT_TYPE) ?
                                       headers.get(AbstractRequest.CONTENT_TYPE) : DEFAULT_CONTENT_TYPE;

                requestBuilder.post(RequestBody.create(MediaType.parse(contentType), bodyContents));
                break;
            default:
                throw new IllegalArgumentException("message build error: unknown verb type");
        }

        for (Map.Entry<String, String> header : headers.entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }
        if (userAgent != null) {
            requestBuilder.header(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }

        final Call call = client.newCall(requestBuilder.build());
        return new OAuthAsyncCompletionHandler<>(callback, converter, call);
    }
}
