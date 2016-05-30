package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.util.Map;

public class MisfitApi extends DefaultApi20 {

    protected MisfitApi() {
    }

    private static class InstanceHolder {
        private static final MisfitApi INSTANCE = new MisfitApi();
    }

    public static MisfitApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
		return "https://api.misfitwearables.com/auth/tokens/exchange";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config, Map<String, String> additionalParams) {
        final ParameterList parameters = new ParameterList(additionalParams);
        parameters.add(OAuthConstants.RESPONSE_TYPE, "code");
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

        return parameters.appendTo("https://api.misfitwearables.com/auth/dialog/authorize");
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        throw new UnsupportedOperationException("use getAuthorizationUrl instead");
    }
}
