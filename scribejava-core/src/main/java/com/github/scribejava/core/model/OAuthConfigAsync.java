package com.github.scribejava.core.model;

import com.ning.http.client.AsyncHttpClientConfig;
import java.io.OutputStream;

public class OAuthConfigAsync extends OAuthConfig {

    private AsyncHttpClientConfig asyncHttpClientConfig;
    private String asyncHttpProviderClassName;

    public OAuthConfigAsync(String key, String secret) {
        super(key, secret);
    }

    public OAuthConfigAsync(String key, String secret, String callback, SignatureType type, String scope,
            String grantType, String state, String responseType, OutputStream stream,
            AsyncHttpClientConfig asyncHttpClientConfig) {
        super(key, secret, callback, type, scope, stream, null, null, grantType, state, responseType);
        this.asyncHttpClientConfig = asyncHttpClientConfig;
    }

    public AsyncHttpClientConfig getAsyncHttpClientConfig() {
        return asyncHttpClientConfig;
    }

    public void setAsyncHttpProviderClassName(String asyncHttpProviderClassName) {
        this.asyncHttpProviderClassName = asyncHttpProviderClassName;
    }

    public String getAsyncHttpProviderClassName() {
        return asyncHttpProviderClassName;
    }
}
