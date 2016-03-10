package com.github.scribejava.apis;

import com.github.scribejava.apis.service.ImgurOAuthServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

public class ImgurApi extends DefaultApi20 {

    private static final String AUTHORIZATION_URL =
            "https://api.imgur.com/oauth2/authorize?client_id=%s&response_type=%s";

    protected ImgurApi() {
    }

    private static class InstanceHolder {
        private static final ImgurApi INSTANCE = new ImgurApi();
    }

    public static ImgurApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.imgur.com/oauth2/token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(AUTHORIZATION_URL, config.getApiKey(), isOob(config) ? "pin" : "code");
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new ImgurOAuthServiceImpl(this, config);
    }

    public static boolean isOob(OAuthConfig config) {
        return "oob".equals(config.getCallback());
    }
}
