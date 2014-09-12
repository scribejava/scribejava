package ru.hh.oauth.subscribe.service;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.AbstractRequest;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.oauth.OAuth20ServiceImpl;

public class HHOAuthServiceImpl extends OAuth20ServiceImpl {

    public HHOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(Token accessToken, AbstractRequest request) {
        request.addHeader("Authorization", "Bearer " + accessToken.getToken());
    }
}
