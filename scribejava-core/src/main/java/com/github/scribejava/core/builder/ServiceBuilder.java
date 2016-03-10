package com.github.scribejava.core.builder;

import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuthService;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Implementation of the Builder pattern, with a fluent interface that creates a {@link OAuthService}
 */
public class ServiceBuilder extends AbstractServiceBuilder<ServiceBuilder> {

    private Integer connectTimeout;
    private Integer readTimeout;

    public ServiceBuilder connectTimeout(Integer connectTimeout) {
        Preconditions.checkNotNull(connectTimeout, "Connection timeout can't be null");
        this.connectTimeout = connectTimeout;
        return this;
    }

    public ServiceBuilder readTimeout(Integer readTimeout) {
        Preconditions.checkNotNull(readTimeout, "Read timeout can't be null");
        this.readTimeout = readTimeout;
        return this;
    }

    @Override
    protected OAuthConfig createConfig() {
        super.checkPreconditions();
        final OAuthConfig config = new OAuthConfig(getApiKey(), getApiSecret(), getCallback(), getSignatureType(),
                getScope(), getDebugStream(), connectTimeout, readTimeout, getGrantType(), getState(),
                getResponseType());
        return config;
    }
}
