package ru.hh.oauth.subscribe.apis;

import ru.hh.oauth.subscribe.core.builder.api.DefaultApi20;
import ru.hh.oauth.subscribe.core.extractors.AccessTokenExtractor;
import ru.hh.oauth.subscribe.core.extractors.JsonTokenExtractor;
import ru.hh.oauth.subscribe.core.model.OAuthConfig;
import ru.hh.oauth.subscribe.core.model.OAuthConstants;
import ru.hh.oauth.subscribe.core.utils.OAuthEncoder;
import ru.hh.oauth.subscribe.core.utils.Preconditions;

public class Foursquare2Api extends DefaultApi20 {

    private static final String AUTHORIZATION_URL = "https://foursquare.com/oauth2/authenticate?client_id=%s&response_type=code&redirect_uri=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://foursquare.com/oauth2/access_token?grant_type=" + OAuthConstants.AUTHORIZATION_CODE;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Foursquare2 does not support OOB");
        return String.format(AUTHORIZATION_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }
}
