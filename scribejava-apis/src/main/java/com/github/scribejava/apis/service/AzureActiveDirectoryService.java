package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;

public class AzureActiveDirectoryService extends OAuth20Service {

    private final DefaultApi20 api;
    String ACCEPTED_FORMAT = "application/json; odata=minimalmetadata; streaming=true; charset=utf-8";

    public AzureActiveDirectoryService(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
        this.api = api;
    }

    @Override
    public OAuthRequest createAccessTokenRequest(String code) {
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        final OAuthConfig config = getConfig();

        request.addHeader(OAuthConstants.CONTENT_TYPE, OAuthConstants.APPLICATION_X_WWW_FORM_URLENCODED);

        request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        final String apiSecret = config.getApiSecret();
        if (apiSecret != null) {
            request.addBodyParameter(OAuthConstants.CLIENT_SECRET, apiSecret);
        }
        request.addBodyParameter(OAuthConstants.CODE, code);
        request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        final String scope = config.getScope();
        if (scope != null) {
            request.addBodyParameter(OAuthConstants.SCOPE, scope);
        }
        request.addBodyParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);

        return request;
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        request.addHeader(OAuthConstants.AUTHORIZATION, OAuthConstants.BEARER + accessToken);
        request.addHeader(OAuthConstants.ACCEPT, ACCEPTED_FORMAT);
    }
}
