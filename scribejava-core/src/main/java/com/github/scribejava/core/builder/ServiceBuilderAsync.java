package com.github.scribejava.core.builder;

import com.ning.http.client.AsyncHttpClientConfig;
import com.github.scribejava.core.model.OAuthConfigAsync;
import com.github.scribejava.core.utils.Preconditions;

public class ServiceBuilderAsync extends AbstractServiceBuilder<ServiceBuilderAsync> {

    private AsyncHttpClientConfig asyncHttpClientConfig;
    private String asyncHttpProviderClassName;

    public ServiceBuilderAsync asyncHttpClientConfig(AsyncHttpClientConfig asyncHttpClientConfig) {
        Preconditions.checkNotNull(asyncHttpClientConfig, "asyncHttpClientConfig can't be null");
        this.asyncHttpClientConfig = asyncHttpClientConfig;
        return this;
    }

    @Override
    public void checkPreconditions() {
        super.checkPreconditions();
        Preconditions.checkNotNull(asyncHttpClientConfig, "You must provide an asyncHttpClientConfig");
    }

    @Override
    protected OAuthConfigAsync createConfig() {
        checkPreconditions();
        final OAuthConfigAsync configAsync = new OAuthConfigAsync(getApiKey(), getApiSecret(), getCallback(),
                getSignatureType(), getScope(), getGrantType(), getState(), getResponseType(), getDebugStream(),
                asyncHttpClientConfig);
        configAsync.setAsyncHttpProviderClassName(asyncHttpProviderClassName);
        return configAsync;
    }

    public ServiceBuilderAsync asyncHttpProviderClassName(String asyncHttpProviderClassName) {
        this.asyncHttpProviderClassName = asyncHttpProviderClassName;
        return this;
    }
}
