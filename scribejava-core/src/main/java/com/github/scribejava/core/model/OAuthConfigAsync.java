package com.github.scribejava.core.model;

import com.ning.http.client.AsyncHttpClientConfig;
import java.io.OutputStream;

public class OAuthConfigAsync extends OAuthConfig {

    private AsyncHttpClientConfig asyncHttpClientConfig;
    private String asyncHttpProviderClassName;

    public OAuthConfigAsync(final String key, final String secret) {
        super(key, secret);
    }

    public OAuthConfigAsync(final String key, final String secret, final String callback, final SignatureType type,
            final String scope, final String grantType, final OutputStream stream, final AsyncHttpClientConfig asyncHttpClientConfig) {
        super(key, secret, callback, type, scope, stream, null, null, grantType);
        this.asyncHttpClientConfig = asyncHttpClientConfig;
    }

    public AsyncHttpClientConfig getAsyncHttpClientConfig() {
        return asyncHttpClientConfig;
    }

    public void setAsyncHttpProviderClassName(final String asyncHttpProviderClassName) {
        this.asyncHttpProviderClassName = asyncHttpProviderClassName;
    }

    public String getAsyncHttpProviderClassName() {
        return asyncHttpProviderClassName;
    }
}
