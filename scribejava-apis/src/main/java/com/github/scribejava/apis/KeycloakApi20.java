/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.scribejava.apis;

import com.github.scribejava.apis.service.KeycloakOAuthServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 *
 * @author jconner
 */
public class KeycloakApi20 extends DefaultApi20 {

    private static final String KEYCLOAK_URL
            = "http://127.0.0.1:8080/auth/realms/REALMNAME/protocol/openid-connect/";

    private static final String AUTHORIZATION_ENDPOINT
            = KEYCLOAK_URL + "auth/?client_id=%s&response_type=code&redirect_uri=%s";
    private static final String TOKEN_ENDPOINT
            = KEYCLOAK_URL + "token";

    protected KeycloakApi20() {
    }

    private static class InstanceHolder {

        private static final KeycloakApi20 INSTANCE = new KeycloakApi20();
    }

    public static KeycloakApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return TOKEN_ENDPOINT;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback. NLS-AAA does not support OOB");
        return String.format(AUTHORIZATION_ENDPOINT, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new KeycloakOAuthServiceImpl(this, config);
    }
}
