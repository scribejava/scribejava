package ru.hh.oauth.subscribe.apis.service;

import ru.hh.oauth.subscribe.core.builder.api.DefaultApi20;
import ru.hh.oauth.subscribe.core.model.AbstractRequest;
import ru.hh.oauth.subscribe.core.model.OAuthConfig;
import ru.hh.oauth.subscribe.core.model.OAuthConstants;
import ru.hh.oauth.subscribe.core.model.Verifier;
import ru.hh.oauth.subscribe.core.oauth.OAuth20ServiceImpl;

public class GoogleOAuthServiceImpl extends OAuth20ServiceImpl {

    public GoogleOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
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
