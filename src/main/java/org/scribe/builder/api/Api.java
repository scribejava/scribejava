package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.oauth.OAuthService;

/**
 * Contains all the configuration needed to instantiate a valid {@link OAuthService}
 *
 * @author Pablo Fernandez
 *
 */
public interface Api {

    /**
     *
     * @param config
     * @return fully configured {@link OAuthService}
     */
    OAuthService createService(OAuthConfig config);
}
