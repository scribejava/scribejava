package com.github.scribejava.apis.service;

import com.github.scribejava.apis.ImgurApi;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20ServiceImpl;

public final class ImgurOAuthServiceImpl extends OAuth20ServiceImpl {

    public ImgurOAuthServiceImpl(final DefaultApi20 api, final OAuthConfig config) {
        super(api, config);
    }

    @Override
    public Token getAccessToken(final Token requestToken, final Verifier verifier) {
        final boolean oob = ((ImgurApi) getApi()).isOob(getConfig());

        final OAuthRequest request = new OAuthRequest(getApi().getAccessTokenVerb(),
                getApi().getAccessTokenEndpoint(), this);
        request.addBodyParameter(OAuthConstants.CLIENT_ID, getConfig().getApiKey());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, getConfig().getApiSecret());
        request.addBodyParameter(OAuthConstants.GRANT_TYPE, oob ? "pin" : OAuthConstants.AUTHORIZATION_CODE);
        request.addBodyParameter(oob ? "pin" : OAuthConstants.CODE, verifier.getValue());

        return getApi().getAccessTokenExtractor().extract(request.send().getBody());
    }

    @Override
    public void signRequest(final Token accessToken, final AbstractRequest request) {
        request.addHeader("Authorization",
                accessToken != null ? "Bearer " + accessToken.getToken() : "Client-ID " + getConfig().getApiKey());
    }
}
