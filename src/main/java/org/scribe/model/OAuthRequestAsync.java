package org.scribe.model;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import org.scribe.exceptions.OAuthConnectionException;
import org.scribe.oauth.OAuthService;

public class OAuthRequestAsync extends AbstractRequest {

    private AsyncHttpClient asyncHttpClient;
    public static final ResponseConverter<Response> RESPONSE_CONVERTER = new ResponseConverter<Response>() {
        @Override
        public Response convert(com.ning.http.client.Response response) throws IOException {
            final FluentCaseInsensitiveStringsMap map = response.getHeaders();
            final Map<String, String> headersMap = new HashMap<>();
            for (FluentCaseInsensitiveStringsMap.Entry<String, List<String>> header : map) {
                final StringBuilder value = new StringBuilder();
                for (String str : header.getValue()) {
                    value.append(str);
                }
                headersMap.put(header.getKey(), value.toString());
            }
            return new Response(response.getStatusCode(), response.getStatusText(), headersMap, response.getResponseBody(), response.
                    getResponseBodyAsStream());
        }
    };

    public OAuthRequestAsync(Verb verb, String url, OAuthService service) {
        super(verb, url, service);
        asyncHttpClient = service.getAsyncHttpClient();
    }

    public <T> Future<T> sendAsync(final OAuthAsyncRequestCallback<T> callback, final ResponseConverter<T> converter) {

        final String completeUrl = getCompleteUrl();
        final AsyncHttpClient.BoundRequestBuilder boundRequestBuilder;
        switch (getVerb()) {
            case GET:
                boundRequestBuilder = asyncHttpClient.prepareGet(completeUrl);
                break;
            case POST:
                boundRequestBuilder = asyncHttpClient.preparePost(completeUrl).setBody(getBodyContents());
                break;
            default:
                throw new IllegalArgumentException("message build error: unknown verb type");
        }
        try {
            return boundRequestBuilder.execute(
                    new AsyncCompletionHandler<T>() {
                        @Override
                        public T onCompleted(com.ning.http.client.Response response) throws Exception {
                            T t = converter.convert(response);
                            if (callback != null) {
                                callback.onCompleted(t);
                            }
                            return t;
                        }

                        @Override
                        public void onThrowable(Throwable t) {
                            if (callback != null) {
                                callback.onThrowable(t);
                            }
                        }
                    });
        } catch (IOException e) {
            throw new OAuthConnectionException(e);
        }
    }

    public Future<Response> sendAsync(final OAuthAsyncRequestCallback<Response> callback) {
        return sendAsync(callback, RESPONSE_CONVERTER);
    }

    public interface ResponseConverter<T> {

        T convert(com.ning.http.client.Response response) throws IOException;
    }
}
