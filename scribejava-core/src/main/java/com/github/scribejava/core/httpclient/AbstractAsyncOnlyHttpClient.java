package com.github.scribejava.core.httpclient;

import com.github.scribejava.core.model.OAuthRequest;
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
        return executeAsync(userAgent, headers, httpVerb, completeUrl, bodyContents, null,
                (OAuthRequest.ResponseConverter<Response>) null).get();
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            String bodyContents) throws InterruptedException, ExecutionException, IOException {
        return executeAsync(userAgent, headers, httpVerb, completeUrl, bodyContents, null,
                (OAuthRequest.ResponseConverter<Response>) null).get();
    }

    @Override
    public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
            File bodyContents) throws InterruptedException, ExecutionException, IOException {
        return executeAsync(userAgent, headers, httpVerb, completeUrl, bodyContents, null,
                (OAuthRequest.ResponseConverter<Response>) null).get();
    }
    
    @Override
	public Response execute(String userAgent, Map<String, String> headers, Verb httpVerb, String completeUrl,
			OAuthRequest.MultipartPayloads multipartPayloads) throws InterruptedException, ExecutionException, IOException {
		throw new UnsupportedOperationException("This HttpClient does not support Multipart payload for the moment");
	}
}
