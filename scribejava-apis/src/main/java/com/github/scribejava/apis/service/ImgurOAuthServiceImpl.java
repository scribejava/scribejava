package com.github.scribejava.apis.service;

import com.github.scribejava.apis.ImgurApi;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuth20Service;

public class ImgurOAuthServiceImpl extends OAuth20Service {

    public ImgurOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        final OAuthRequest request = new OAuthRequest(getApi().getAccessTokenVerb(),
                getApi().getAccessTokenEndpoint(), this);
        request.addBodyParameter(OAuthConstants.CLIENT_ID, getConfig().getApiKey());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, getConfig().getApiSecret());

        if(ImgurApi.isOob(getConfig())) {
            request.addBodyParameter(OAuthConstants.GRANT_TYPE, "pin");
            request.addBodyParameter("pin", verifier.getValue());
        } else {
            request.addBodyParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);
            request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
        }

        return getApi().getAccessTokenExtractor().extract(request.send().getBody());
    }

    @Override
    public void signRequest(Token accessToken, AbstractRequest request) {
        request.addHeader("Authorization",
                accessToken == null ? "Client-ID " + getConfig().getApiKey() : "Bearer " + accessToken.getToken());
    }
}
