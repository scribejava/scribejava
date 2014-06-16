package ru.hh.oauth.subscribe.api;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;
import ru.hh.oauth.subscribe.service.MailruOAuthServiceImpl;

public class MailruApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://connect.mail.ru/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://connect.mail.ru/oauth/token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Valid url is required for a callback. Mail.ru does not support OOB");
        if (config.hasScope()) { // Appending scope if present
            return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.
                    getScope()));
        } else {
            return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
        }
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new MailruOAuthServiceImpl(this, config);
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }
}
