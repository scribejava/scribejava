package com.github.scribejava.apis;

import com.github.scribejava.apis.service.ImgurOAuthServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.AccessTokenExtractor;
import com.github.scribejava.core.extractors.JsonTokenExtractor;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;

public final class ImgurApi extends DefaultApi20 {

    private static final String AUTHORIZATION_URL =
            "https://api.imgur.com/oauth2/authorize?client_id=%s&response_type=%s";

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.imgur.com/oauth2/token";
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        return String.format(AUTHORIZATION_URL, config.getApiKey(), isOob(config) ? "pin" : "code");
    }

    @Override
    public OAuthService createService(final OAuthConfig config) {
        return new ImgurOAuthServiceImpl(this, config);
    }

    public static boolean isOob(final OAuthConfig config) {
        return "oob".equals(config.getCallback());
    }
}
