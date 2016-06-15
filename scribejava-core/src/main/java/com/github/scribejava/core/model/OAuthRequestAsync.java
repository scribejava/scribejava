package com.github.scribejava.core.model;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.oauth.OAuthService;

import java.io.IOException;
import java.util.concurrent.Future;

public class OAuthRequestAsync extends AbstractRequest {

    public OAuthRequestAsync(Verb verb, String url, OAuthService service) {
        super(verb, url, service);
    }

    public <T> Future<T> sendAsync(OAuthAsyncRequestCallback<T> callback, ResponseConverter<T> converter) {
        final ForceTypeOfHttpRequest forceTypeOfHttpRequest = ScribeJavaConfig.getForceTypeOfHttpRequests();
        if (ForceTypeOfHttpRequest.FORCE_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
            throw new OAuthException("Cannot use async operations, only sync");
        }
        final OAuthService service = getService();
        final OAuthConfig config = service.getConfig();
        if (ForceTypeOfHttpRequest.PREFER_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
            config.log("Cannot use async operations, only sync");
        }
        return service.executeAsync(getHeaders(), getVerb(), getCompleteUrl(), getBodyContents(), callback, converter);
    }

    public Future<Response> sendAsync(OAuthAsyncRequestCallback<Response> callback) {
        return sendAsync(callback, null);
    }

    public interface ResponseConverter<T> {

        T convert(Response response) throws IOException;
    }
}
