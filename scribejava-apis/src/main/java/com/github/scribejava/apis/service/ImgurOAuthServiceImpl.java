package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;

/**
 *
 * @deprecated renamed to {@link ImgurOAuthService}
 */
@Deprecated
public class ImgurOAuthServiceImpl extends ImgurOAuthService {

    public ImgurOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }
}
