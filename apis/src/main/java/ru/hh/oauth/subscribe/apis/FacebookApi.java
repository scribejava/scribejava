package ru.hh.oauth.subscribe.apis;

import ru.hh.oauth.subscribe.core.builder.api.DefaultApi20;
import ru.hh.oauth.subscribe.core.model.OAuthConfig;
import ru.hh.oauth.subscribe.core.model.OAuthConstants;
import ru.hh.oauth.subscribe.core.utils.OAuthEncoder;
import ru.hh.oauth.subscribe.core.utils.Preconditions;

public class FacebookApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://www.facebook.com/v2.2/dialog/oauth?client_id=%s&redirect_uri=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://graph.facebook.com/v2.2/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback. Facebook does not support OOB");
        final StringBuilder sb = new StringBuilder(String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(
                config.getCallback())));
        if (config.hasScope()) {
            sb.append('&').append(OAuthConstants.SCOPE).append('=').append(OAuthEncoder.encode(config.getScope()));
        }

        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(OAuthConstants.STATE).append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }
}
