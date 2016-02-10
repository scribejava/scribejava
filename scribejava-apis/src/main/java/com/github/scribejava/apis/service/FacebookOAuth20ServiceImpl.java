package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 *
 * @author Daniel TYreus
 */
public class FacebookOAuth20ServiceImpl extends OAuth20Service {

    public FacebookOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    public OAuth2AccessToken refreshOAuth2AccessToken(final OAuth2AccessToken refreshToken) {

        final OAuthRequest request = new OAuthRequest(getApi().getAccessTokenVerb(),
                getApi().getAccessTokenEndpoint(), this);
        final OAuthConfig config = getConfig();
        request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        //facebook uses the access_token as a refresh_token. fun, right?
        request.addParameter("fb_exchange_token", refreshToken.getToken());
        request.addParameter("grant_type", "fb_exchange_token");

        final Response response = request.send();

        return (OAuth2AccessToken) getApi().getAccessTokenExtractor().extract(response.getBody());

    }
}
