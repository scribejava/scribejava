package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;
import com.github.scribejava.apis.service.TutByOAuthServiceImpl;
import com.github.scribejava.core.oauth.OAuth20Service;

public class TutByApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "http://profile.tut.by/auth?client_id=%s&response_type=code&redirect_uri=%s";

    protected TutByApi() {
    }

    private static class InstanceHolder {
        private static final TutByApi INSTANCE = new TutByApi();
    }

    public static TutByApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://profile.tut.by/getToken";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Valid url is required for a callback. Tut.by does not support OOB");
        return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new TutByOAuthServiceImpl(this, config);
    }
}
