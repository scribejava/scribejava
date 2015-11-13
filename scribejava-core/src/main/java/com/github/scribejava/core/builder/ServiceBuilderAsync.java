package com.github.scribejava.core.builder;

import com.ning.http.client.AsyncHttpClientConfig;
import com.github.scribejava.core.model.OAuthConfigAsync;
import com.github.scribejava.core.oauth.OAuthService;
import com.github.scribejava.core.utils.Preconditions;

public class ServiceBuilderAsync extends AbstractServiceBuilder<ServiceBuilderAsync> {

    private AsyncHttpClientConfig asyncHttpClientConfig;
    private String asyncHttpProviderClassName;

    public ServiceBuilderAsync asyncHttpClientConfig(final AsyncHttpClientConfig asyncHttpClientConfig) {
        Preconditions.checkNotNull(asyncHttpClientConfig, "asyncHttpClientConfig can't be null");
        this.asyncHttpClientConfig = asyncHttpClientConfig;
        return this;
    }

    @Override
    public void checkPreconditions() {
        super.checkPreconditions();
        Preconditions.checkNotNull(asyncHttpClientConfig, "You must provide an asyncHttpClientConfig");
    }

    public OAuthService build() {
        checkPreconditions();
        final OAuthConfigAsync configAsync = new OAuthConfigAsync(getApiKey(), getApiSecret(), getCallback(), getSignatureType(), getScope(),
                getGrantType(), getDebugStream(), asyncHttpClientConfig);
        configAsync.setState(getState());
        configAsync.setAsyncHttpProviderClassName(asyncHttpProviderClassName);

        return getApi().createService(configAsync);
    }

    public ServiceBuilderAsync asyncHttpProviderClassName(final String asyncHttpProviderClassName) {
        this.asyncHttpProviderClassName = asyncHttpProviderClassName;
        return this;
    }
}
