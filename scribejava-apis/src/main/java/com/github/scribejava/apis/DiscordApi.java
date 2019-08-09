package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class DiscordApi extends DefaultApi20 {

    private DiscordApi() {
    }

    private static class InstanceHolder {
        private static final DiscordApi INSTANCE = new DiscordApi();
    }

    public static DiscordApi instance() {
        return DiscordApi.InstanceHolder.INSTANCE;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://discordapp.com/api/oauth2/authorize";
    }

    @Override
    public String getRevokeTokenEndpoint() {
        return "https://discordapp.com/api/oauth2/token/revoke";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://discordapp.com/api/oauth2/token";
    }
}
