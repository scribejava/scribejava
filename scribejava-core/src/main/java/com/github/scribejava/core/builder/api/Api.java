package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuthService;

/**
 * Contains all the configuration needed to instantiate a valid {@link OAuthService}
 *
 * @author Pablo Fernandez
 *
 */
public interface Api {

    /**
     *
     * @param config config to build the Service from
     * @return fully configured {@link OAuthService}
     */
    OAuthService createService(OAuthConfig config);
}
