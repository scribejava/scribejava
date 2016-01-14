package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.AccessTokenExtractor;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractorImpl;
import com.github.scribejava.core.model.AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/***
 * Facebook v2.5 API
 *
 */
public class FacebookApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "https://www.facebook.com/v2.5/dialog/oauth?client_id=%s&redirect_uri=%s";

    protected FacebookApi() {
    }

    private static class InstanceHolder {
        private static final FacebookApi INSTANCE = new FacebookApi();
    }

    public static FacebookApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://graph.facebook.com/v2.5/oauth/access_token";
    }
    
    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new OAuth2AccessTokenExtractorImpl("access_token","refresh_token","expires",null,"token_type");
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback. Facebook does not support OOB");
        final StringBuilder sb = new StringBuilder(String.format(AUTHORIZE_URL, config.getApiKey(),
                OAuthEncoder.encode(config.getCallback())));
        if (config.hasScope()) {
            sb.append('&').append(OAuthConstants.SCOPE).append('=').append(OAuthEncoder.encode(config.getScope()));
        }

        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(OAuthConstants.STATE).append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new FacebookOAuth20Service(this, config);
    }
    
    private static class FacebookOAuth20Service extends OAuth20Service {

        public FacebookOAuth20Service(DefaultApi20 api, OAuthConfig config) {
            super(api, config);
        }

        @Override
        public AccessToken refreshOAuth2AccessToken(final OAuth2AccessToken refreshToken) {

            final OAuthRequest request = new OAuthRequest(getApi().getAccessTokenVerb(), getApi().getAccessTokenEndpoint(), this);
            final OAuthConfig config = getConfig();
            request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
            request.addParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
            //facebook uses the access_token as a refresh_token. fun, right?
            request.addParameter("fb_exchange_token", refreshToken.getToken());
            request.addParameter("grant_type", "fb_exchange_token");

            final Response response = request.send();

            return getApi().getAccessTokenExtractor().extract(response.getBody());

        }
    }
}
