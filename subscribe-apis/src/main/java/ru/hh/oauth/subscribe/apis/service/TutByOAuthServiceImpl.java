package ru.hh.oauth.subscribe.apis.service;

import ru.hh.oauth.subscribe.core.builder.api.DefaultApi20;
import ru.hh.oauth.subscribe.core.model.AbstractRequest;
import ru.hh.oauth.subscribe.core.model.OAuthConfig;
import ru.hh.oauth.subscribe.core.model.OAuthConstants;
import ru.hh.oauth.subscribe.core.model.Token;
import ru.hh.oauth.subscribe.core.oauth.OAuth20ServiceImpl;

public class TutByOAuthServiceImpl extends OAuth20ServiceImpl {

    public TutByOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(Token accessToken, AbstractRequest request) {
        request.addQuerystringParameter(OAuthConstants.TOKEN, accessToken.getToken());
    }

}
