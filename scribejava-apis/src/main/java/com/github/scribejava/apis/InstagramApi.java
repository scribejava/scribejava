package com.github.scribejava.apis;

import java.io.OutputStream;
import com.github.scribejava.apis.instagram.InstagramAccessTokenJsonExtractor;
import com.github.scribejava.apis.instagram.InstagramService;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

public class InstagramApi extends DefaultApi20 {

    public static final String LONG_LIVED_ACCESS_TOKEN_ENDPOINT = "https://graph.instagram.com/access_token";

    private static class InstanceHolder {

        private static final InstagramApi INSTANCE = new InstagramApi();
    }

    public static InstagramApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.instagram.com/oauth/access_token";
    }

    @Override
    public String getRefreshTokenEndpoint() {
        return "https://graph.instagram.com/refresh_access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.instagram.com/oauth/authorize";
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return InstagramAccessTokenJsonExtractor.instance();
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }

    @Override
    public InstagramService createService(String apiKey, String apiSecret, String callback, String defaultScope,
            String responseType, OutputStream debugStream, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        return new InstagramService(this, apiKey, apiSecret, callback, defaultScope, responseType, debugStream,
                userAgent, httpClientConfig, httpClient);
    }
}
