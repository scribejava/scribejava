package ru.hh.oauth.subscribe.service;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.AbstractRequest;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;

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
