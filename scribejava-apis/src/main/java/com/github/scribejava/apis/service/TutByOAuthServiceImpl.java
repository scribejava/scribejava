package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;

/**
 *
 * @deprecated renamed to {@link TutByOAuthService}
 */
@Deprecated
public class TutByOAuthServiceImpl extends TutByOAuthService {

    public TutByOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }
}
