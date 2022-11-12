package com.github.scribejava.core.httpclient;

import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class AbstractAsyncOnlyHttpClient implements HttpClient {

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            byte[] bodyContents) throws InterruptedException, ExecutionException, IOException {

        final OAuthAsyncRequestThrowableHolderCallback oAuthAsyncRequestThrowableHolderCallback
                = new OAuthAsyncRequestThrowableHolderCallback();

        final Response response = executeAsync(userAgent, headers, httpVerb, completeUrl, bodyContents,
                oAuthAsyncRequestThrowableHolderCallback, null).get();

        final Throwable throwable = oAuthAsyncRequestThrowableHolderCallback.getThrowable();
        if (throwable != null) {
            throw new ExecutionException(throwable);
        }
        return response;
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            com.github.scribejava.core.httpclient.multipart.MultipartPayload bodyContents)
            throws InterruptedException, ExecutionException, IOException {

        final OAuthAsyncRequestThrowableHolderCallback oAuthAsyncRequestThrowableHolderCallback
                = new OAuthAsyncRequestThrowableHolderCallback();

        final Response response = executeAsync(userAgent, headers, httpVerb, completeUrl, bodyContents,
                oAuthAsyncRequestThrowableHolderCallback, null).get();

        final Throwable throwable = oAuthAsyncRequestThrowableHolderCallback.getThrowable();
        if (throwable != null) {
            throw new ExecutionException(throwable);
        }

        return response;
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents) throws InterruptedException, ExecutionException, IOException {

        final OAuthAsyncRequestThrowableHolderCallback oAuthAsyncRequestThrowableHolderCallback
                = new OAuthAsyncRequestThrowableHolderCallback();

        final Response response = executeAsync(userAgent, headers, httpVerb, completeUrl, bodyContents,
                oAuthAsyncRequestThrowableHolderCallback, null).get();

        final Throwable throwable = oAuthAsyncRequestThrowableHolderCallback.getThrowable();
        if (throwable != null) {
            throw new ExecutionException(throwable);
        }

        return response;
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents) throws InterruptedException, ExecutionException, IOException {

        final OAuthAsyncRequestThrowableHolderCallback oAuthAsyncRequestThrowableHolderCallback
                = new OAuthAsyncRequestThrowableHolderCallback();

        final Response response = executeAsync(userAgent, headers, httpVerb, completeUrl, bodyContents,
                oAuthAsyncRequestThrowableHolderCallback, null).get();

        final Throwable throwable = oAuthAsyncRequestThrowableHolderCallback.getThrowable();
        if (throwable != null) {
            throw new ExecutionException(throwable);
        }

        return response;
    }

    private class OAuthAsyncRequestThrowableHolderCallback implements OAuthAsyncRequestCallback<Response> {

        private Throwable throwable;

        @Override
        public void onCompleted(Response response) {
        }

        @Override
        public void onThrowable(Throwable t) {
            throwable = t;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }
}
