package com.github.scribejava.apis;

import com.github.scribejava.apis.service.ImgurOAuthServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.util.Map;

public class ImgurApi extends DefaultApi20 {

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
    public String getAuthorizationUrl(OAuthConfig config, Map<String, String> additionalParams) {
        final ParameterList parameters = new ParameterList(additionalParams);
        parameters.add(OAuthConstants.RESPONSE_TYPE, isOob(config) ? "pin" : "code");
        parameters.add(OAuthConstants.CLIENT_ID, config.getApiKey());

        final String callback = config.getCallback();
        if (callback != null) {
            parameters.add(OAuthConstants.REDIRECT_URI, callback);
        }

        final String scope = config.getScope();
        if (scope != null) {
            parameters.add(OAuthConstants.SCOPE, scope);
        }

        final String state = config.getState();
        if (state != null) {
            parameters.add(OAuthConstants.STATE, state);
        }

        return parameters.appendTo("https://api.imgur.com/oauth2/authorize");
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        throw new UnsupportedOperationException("use getAuthorizationUrl instead");
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new ImgurOAuthServiceImpl(this, config);
    }

    public static boolean isOob(OAuthConfig config) {
        return "oob".equals(config.getCallback());
    }
}
