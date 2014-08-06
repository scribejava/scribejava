package ru.hh.oauth.subscribe.api;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import ru.hh.oauth.subscribe.service.HHOAuthServiceImpl;

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
