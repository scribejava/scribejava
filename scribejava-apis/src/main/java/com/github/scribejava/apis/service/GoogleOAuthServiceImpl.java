package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;

public class GoogleOAuthServiceImpl extends OpenIdConnectServiceImpl {

    public GoogleOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }
}
