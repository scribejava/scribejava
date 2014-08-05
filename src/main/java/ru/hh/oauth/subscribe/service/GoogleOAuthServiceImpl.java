package ru.hh.oauth.subscribe.service;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;

public class GoogleOAuthServiceImpl extends OAuth20ServiceImpl {

    public GoogleOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    protected OAuthRequest createAccessTokenRequest(final Verifier verifier) {
        final OAuthRequest request = super.createAccessTokenRequest(verifier);
        if (!getConfig().hasGrantType()) {
            request.addParameter(OAuthConstants.GRANT_TYPE, "authorization_code");
        }
        return request;
    }
}
