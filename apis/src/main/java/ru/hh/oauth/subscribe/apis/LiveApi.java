package ru.hh.oauth.subscribe.apis;

import ru.hh.oauth.subscribe.core.builder.api.DefaultApi20;
import ru.hh.oauth.subscribe.core.extractors.AccessTokenExtractor;
import ru.hh.oauth.subscribe.core.extractors.JsonTokenExtractor;
import ru.hh.oauth.subscribe.core.model.OAuthConfig;
import ru.hh.oauth.subscribe.core.model.OAuthConstants;
import ru.hh.oauth.subscribe.core.utils.OAuthEncoder;
import ru.hh.oauth.subscribe.core.utils.Preconditions;

public class LiveApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://oauth.live.com/authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://login.live.com/oauth20_token.srf?grant_type=" + OAuthConstants.AUTHORIZATION_CODE;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Live does not support OOB");

        // Append scope if present
        if (config.hasScope()) {
            return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.
                    getScope()));
        } else {
            return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
        }
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }
}
