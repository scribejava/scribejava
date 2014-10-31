package ru.hh.oauth.subscribe.apis;

import ru.hh.oauth.subscribe.core.builder.api.DefaultApi20;
import ru.hh.oauth.subscribe.core.extractors.AccessTokenExtractor;
import ru.hh.oauth.subscribe.core.extractors.JsonTokenExtractor;
import ru.hh.oauth.subscribe.core.model.OAuthConfig;
import ru.hh.oauth.subscribe.core.model.OAuthConstants;
import ru.hh.oauth.subscribe.core.model.Verb;
import ru.hh.oauth.subscribe.core.oauth.OAuthService;

import ru.hh.oauth.subscribe.apis.service.HHOAuthServiceImpl;

public class HHApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://m.hh.ru/oauth/authorize?response_type=code&client_id=%s";
    private static final String TOKEN_URL = "https://m.hh.ru/oauth/token?grant_type=" + OAuthConstants.AUTHORIZATION_CODE;

    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(AUTHORIZE_URL, config.getApiKey());
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new HHOAuthServiceImpl(this, config);
    }
}
