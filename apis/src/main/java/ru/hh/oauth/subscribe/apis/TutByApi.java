package ru.hh.oauth.subscribe.apis;

import ru.hh.oauth.subscribe.core.builder.api.DefaultApi20;
import ru.hh.oauth.subscribe.core.extractors.AccessTokenExtractor;
import ru.hh.oauth.subscribe.core.extractors.JsonTokenExtractor;
import ru.hh.oauth.subscribe.core.model.OAuthConfig;
import ru.hh.oauth.subscribe.core.model.Verb;
import ru.hh.oauth.subscribe.core.oauth.OAuthService;
import ru.hh.oauth.subscribe.core.utils.OAuthEncoder;
import ru.hh.oauth.subscribe.core.utils.Preconditions;
import ru.hh.oauth.subscribe.apis.service.TutByOAuthServiceImpl;

public class TutByApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "http://profile.tut.by/auth?client_id=%s&response_type=code&redirect_uri=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "http://profile.tut.by/getToken";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Valid url is required for a callback. Tut.by does not support OOB");
        return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new TutByOAuthServiceImpl(this, config);
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }
}
