package com.github.scribejava.core.model;

import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.IOException;

public abstract class AbstractTokenStorage {

    private final OAuth20Service service;

    protected AbstractTokenStorage(OAuth20Service service) {
        this.service = service;
    }

    public OAuth2AccessToken getOrGenerateToken() throws IOException {
        OAuth2AccessToken token = loadToken();
        if (token == null) {
            token = service.getAccessToken();
            storeToken(token);
            return token;
        }
        if (!isExpired(token)) {
            return token;
        }

        final OAuth2AccessToken refreshedToken = service.refreshAccessToken(token.getRefreshToken());
        storeToken(refreshedToken);
        return refreshedToken;
    }

    protected abstract OAuth2AccessToken loadToken();
    protected abstract void storeToken(OAuth2AccessToken token);
    protected abstract boolean isExpired(OAuth2AccessToken token);
}
