package ru.hh.oauth.subscribe.apis.service;

import ru.hh.oauth.subscribe.core.builder.api.DefaultApi20;
import ru.hh.oauth.subscribe.core.model.AbstractRequest;
import ru.hh.oauth.subscribe.core.model.OAuthConfig;
import ru.hh.oauth.subscribe.core.model.OAuthConstants;
import ru.hh.oauth.subscribe.core.model.Token;
import ru.hh.oauth.subscribe.core.model.Verifier;
import ru.hh.oauth.subscribe.core.oauth.OAuth20ServiceImpl;

public class LinkedIn20ServiceImpl extends OAuth20ServiceImpl {

    public LinkedIn20ServiceImpl(final DefaultApi20 api, final OAuthConfig config) {
        super(api, config);
    }

    @Override
    public void signRequest(Token accessToken, AbstractRequest request) {
        request.addQuerystringParameter("oauth2_access_token", accessToken.getToken());
    }

    @Override
    protected <T extends AbstractRequest> T createAccessTokenRequest(final Verifier verifier, T request) {
        super.createAccessTokenRequest(verifier, request);
        if (!getConfig().hasGrantType()) {
            request.addParameter(OAuthConstants.GRANT_TYPE, "authorization_code");
        }
        return (T) request;
    }
}
