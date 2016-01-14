package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuth20ServiceImpl;

public class HHOAuthServiceImpl extends OAuth20ServiceImpl {

    public HHOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(AccessToken accessToken, AbstractRequest request) {
        request.addHeader("Authorization", "Bearer " + accessToken.getToken());
    }
}
