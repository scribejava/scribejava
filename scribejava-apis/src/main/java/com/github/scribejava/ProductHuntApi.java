package com.github.scribejava.apis;

import com.github.scribejava.apis.service.ProductHuntServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.AccessTokenExtractor;
import com.github.scribejava.core.extractors.JsonTokenExtractor;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;
import com.github.scribejava.core.utils.OAuthEncoder;

public class ProductHuntApi extends DefaultApi20 {
    private static final String AUTHORIZE_URL = "https://api.producthunt.com/v1/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String ACCESS_TOKEN_URL = "https://api.producthunt.com/v1/oauth/token";

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback. Product Hunt does not support OOB");
        final StringBuilder sb = new StringBuilder(String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(
                config.getCallback())));
        if (config.hasScope()) {
            sb.append('&').append(OAuthConstants.SCOPE).append('=').append(OAuthEncoder.encode(config.getScope()));
        }

        return sb.toString();
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override
    public OAuthService createService(final OAuthConfig config) {
        return new ProductHuntServiceImpl(this, config);
    }
}

