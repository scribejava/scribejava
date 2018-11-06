package com.github.scribejava.apis;

import com.github.scribejava.apis.service.DiscordService;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.OutputStream;

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

    @Override
    public OAuth20Service createService(String apiKey, String apiSecret, String callback,
                                        String scope, OutputStream debugStream, String state,
                                        String responseType, String userAgent,
                                        HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new DiscordService(this, apiKey, apiSecret, callback, scope, state, responseType,
                userAgent, httpClientConfig, httpClient);
    }
}
