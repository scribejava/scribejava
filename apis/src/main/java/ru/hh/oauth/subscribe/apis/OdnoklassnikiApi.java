package ru.hh.oauth.subscribe.apis;

import ru.hh.oauth.subscribe.apis.service.OdnoklassnikiServiceImpl;
import ru.hh.oauth.subscribe.core.builder.api.DefaultApi20;
import ru.hh.oauth.subscribe.core.extractors.AccessTokenExtractor;
import ru.hh.oauth.subscribe.core.extractors.JsonTokenExtractor;
import ru.hh.oauth.subscribe.core.model.OAuthConfig;
import ru.hh.oauth.subscribe.core.model.Verb;
import ru.hh.oauth.subscribe.core.oauth.OAuthService;
import ru.hh.oauth.subscribe.core.utils.OAuthEncoder;
import ru.hh.oauth.subscribe.core.utils.Preconditions;

public class OdnoklassnikiApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "http://www.odnoklassniki.ru/oauth/authorize?client_id=%s&response_type=code&redirect_uri=%s";
    private static final String SCOPED_AUTHORIZE_URL = String.format("%s&scope=%%s", AUTHORIZE_URL);

    @Override
    public String getAccessTokenEndpoint() {
        return "http://api.odnoklassniki.ru/oauth/token.do";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Valid url is required for a callback. Odnoklassniki does not support OOB");
        if (config.hasScope()) {
            return String.format(
                    SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
        } else {
            return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
        }
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new OdnoklassnikiServiceImpl(this, config);
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }
}
