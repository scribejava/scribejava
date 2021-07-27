package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;
import java.util.Map;

public class DoktornaraboteApi extends DefaultApi20 {

    private static final String REDIRECT_PARAM_NAME = "redirect";

    protected DoktornaraboteApi() {
    }

    private static class InstanceHolder {
        private static final DoktornaraboteApi INSTANCE = new DoktornaraboteApi();
    }

    public static DoktornaraboteApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://id.doktornarabote.ru/api/v1/auth/token";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://www.doktornarabote.ru";
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }

    @Override
    public String getAuthorizationUrl(String responseType, String apiKey, String callback, String scope, String state,
                                      Map<String, String> additionalParams) {
        final ParameterList parameters = new ParameterList(additionalParams);
        parameters.add(OAuthConstants.RESPONSE_TYPE, responseType);
        parameters.add(OAuthConstants.CLIENT_ID, apiKey);

        if (callback != null) {
            parameters.add(REDIRECT_PARAM_NAME, callback);
        }

        if (scope != null) {
            parameters.add(OAuthConstants.SCOPE, scope);
        }

        if (state != null) {
            parameters.add(OAuthConstants.STATE, state);
        }

        return parameters.appendTo(getAuthorizationBaseUrl());
    }

}
