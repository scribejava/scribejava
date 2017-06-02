package com.github.scribejava.apis;

import com.github.scribejava.apis.service.DiscordOAuthServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

public class DiscordApi extends DefaultApi20 {

    protected DiscordApi() {
    }

    private static class InstanceHolder {
        private static final DiscordApi INSTANCE = new DiscordApi();
    }

    public static DiscordApi instance() {
        return DiscordApi.InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://discordapp.com/api/oauth2/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://discordapp.com/api/oauth2/authorize";
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new DiscordOAuthServiceImpl(this, config);
    }
}
