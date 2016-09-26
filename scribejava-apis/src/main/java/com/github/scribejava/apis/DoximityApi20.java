package com.github.scribejava.apis;

import com.github.scribejava.apis.service.Doximity20ServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.util.Map;

public class DoximityApi20 extends DefaultApi20 {

    protected DoximityApi20(){
    }

    private static class InstanceHolder {
        private static final DoximityApi20 INSTANCE = new DoximityApi20();
    }

    public static final DoximityApi20 instance(){
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://auth.doximity.com/oauth/token?grant_type=authorization_code";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://auth.doximity.com/oauth/authorize?";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config, Map<String, String> additionalParams){

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

        return parameters.appendTo("https://auth.doximity.com/oauth/authorize?");
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new Doximity20ServiceImpl(this, config);
    }
}
