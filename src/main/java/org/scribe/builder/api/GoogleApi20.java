package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;
import ru.hh.oauth.subscribe.api.google.GoogleJsonTokenExtractor;

public class GoogleApi20 extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=%s&redirect_uri=%s&scope=%s";
    private static final String PARAM_STATE = "state";

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://accounts.google.com/o/oauth2/token";
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback. Google+ does not support OOB");
        final StringBuilder sb = new StringBuilder(String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(
                config.getCallback()), OAuthEncoder.encode(config.getScope())));

        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(PARAM_STATE).append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new GoogleJsonTokenExtractor();
    }
}
