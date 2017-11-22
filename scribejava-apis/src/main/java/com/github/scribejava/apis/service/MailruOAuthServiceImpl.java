package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;

/**
 *
 * @deprecated renamed to {@link MailruOAuthService}
 */
@Deprecated
public class MailruOAuthServiceImpl extends MailruOAuthService {

    public MailruOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }
}
