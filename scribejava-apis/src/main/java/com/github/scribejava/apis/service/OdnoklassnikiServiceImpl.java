package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;

/**
 *
 * @deprecated renamed to {@link OdnoklassnikiOAuthService}
 */
@Deprecated
public class OdnoklassnikiServiceImpl extends OdnoklassnikiOAuthService {

    public OdnoklassnikiServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }
}
