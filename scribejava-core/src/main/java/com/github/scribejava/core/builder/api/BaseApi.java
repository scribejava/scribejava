package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuthService;

public interface BaseApi<T extends OAuthService> {

    T createService(OAuthConfig config);
}
