package com.github.scribejava.httpclient.loopj;

import com.github.scribejava.core.httpclient.AbstractAsyncOnlyHttpClient;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.loopj.android.http.HttpDelete;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestHandle;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.Future;

import cz.msebera.android.httpclient.client.methods.HttpEntityEnclosingRequestBase;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.FileEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class AsyncHttpClient extends AbstractAsyncOnlyHttpClient {

    final private com.loopj.android.http.AsyncHttpClient client;

    public AsyncHttpClient() {
        client = new com.loopj.android.http.AsyncHttpClient();
    }

    public AsyncHttpClient(com.loopj.android.http.AsyncHttpClient client) {
        this.client = client;
    }

    @Override
    public void close() {
        client.cancelAllRequests(true);
    }

    @Override
    public  <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            byte[] bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodySetter.BYTE_ARRAY, bodyContents, callback,
                converter);
    }

    @Override
    public  <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodySetter.STRING, bodyContents, callback,
                converter);
    }

    @Override
    public  <T> Future<T> executeAsync(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents, OAuthAsyncRequestCallback<T> callback, OAuthRequest.ResponseConverter<T> converter) {

        return doExecuteAsync(userAgent, headers, httpVerb, completeUrl, BodySetter.FILE, bodyContents, callback,
                converter);
    }

    private  <T> Future<T>  doExecuteAsync(String userAgent, Map<String, String> headers, Verb httpVerb,
            String completeUrl, BodySetter bodySetter, Object bodyContents, OAuthAsyncRequestCallback<T> callback,
            OAuthRequest.ResponseConverter<T> converter) {

        final HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase;

        switch (httpVerb) {
            case GET:
                httpEntityEnclosingRequestBase = new HttpGet(completeUrl);
                break;
            case POST:
                httpEntityEnclosingRequestBase = new HttpPost(completeUrl);
                break;
            case PUT:
                httpEntityEnclosingRequestBase = new HttpPut(completeUrl);
                break;
            case DELETE:
                httpEntityEnclosingRequestBase = new HttpDelete(completeUrl);
                break;
            default:
                throw new IllegalArgumentException("message build error: unknown verb type");
        }

        if (httpVerb.isPermitBody()) {
            if (!headers.containsKey(CONTENT_TYPE)) {
                client.addHeader(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
            }
            bodySetter.setBody(httpEntityEnclosingRequestBase, bodyContents);
        }

        for (Map.Entry<String, String> header : headers.entrySet()) {
            client.addHeader(header.getKey(), header.getValue());
        }
        if (userAgent != null) {
            client.addHeader(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }

        RequestHandle requestHandle;

        final OAuthAsyncCompletionHandler handler = new OAuthAsyncCompletionHandler<>(callback, converter);
        switch (httpVerb) {
            case GET:
                requestHandle = client.get(completeUrl, handler);
                break;
            case POST:
                requestHandle = client.post(completeUrl, handler);
                break;
            case PUT:
                requestHandle = client.put(completeUrl, handler);
                break;
            case DELETE:
                requestHandle = client.delete(completeUrl, handler);
                break;
            default:
                throw new IllegalArgumentException("message build error: unknown verb type");
        }

        final AsyncHttpFuture<T> asyncHttpFuture = new AsyncHttpFuture<>(requestHandle);
        return asyncHttpFuture;
    }

    private enum BodySetter {
        BYTE_ARRAY {
            void setBody(
                    HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase,
                    Object bodyContents) {
                httpEntityEnclosingRequestBase.setEntity(new ByteArrayEntity((byte[]) bodyContents));
            }
        },
        STRING {
            void setBody(
                    HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase,
                    Object bodyContents) {
                try {
                    httpEntityEnclosingRequestBase.setEntity(new StringEntity((String) bodyContents));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        },
        FILE {
            @Override
            void setBody(
                    HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase,
                    Object bodyContents) {
                httpEntityEnclosingRequestBase.setEntity(new FileEntity((File) bodyContents));
            }
        };

        abstract void setBody(
                HttpEntityEnclosingRequestBase entityEnclosingRequestBase,
                Object bodyContents);
    }


}
