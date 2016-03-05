package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.utils.OAuthEncoder;

public class GitLabApi extends DefaultApi20 {

    private static final String ACCESS_TOKEN_ENDPOINT_PATH = "/oauth/token";
    private static final String AUTHORIZATION_URL =
            "%s/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String DEFAULT_BASE_URL = "https://gitlab.com";

    private final String baseUrl;

    public GitLabApi(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected GitLabApi() {
        this(DEFAULT_BASE_URL);
    }

    private static class InstanceHolder {
        private static final GitLabApi INSTANCE = new GitLabApi();
    }

    public static GitLabApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return baseUrl + ACCESS_TOKEN_ENDPOINT_PATH;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(AUTHORIZATION_URL, baseUrl, config.getApiKey(),
                OAuthEncoder.encode(config.getCallback()));
    }
}
