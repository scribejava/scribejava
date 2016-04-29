/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 *
 * @author jconner
 */
public class KeycloakOAuthServiceImpl extends OAuth20Service {

    public KeycloakOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    protected <T extends AbstractRequest> T createAccessTokenRequest(String code, T request) {
        super.createAccessTokenRequest(code, request);
        if (!getConfig().hasGrantType()) {
            request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);
        }
        return request;
    }

}
