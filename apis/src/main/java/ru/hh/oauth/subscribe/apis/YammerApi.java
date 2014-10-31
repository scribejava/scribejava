package ru.hh.oauth.subscribe.apis;

import ru.hh.oauth.subscribe.core.builder.api.DefaultApi10a;
import ru.hh.oauth.subscribe.core.model.Token;
import ru.hh.oauth.subscribe.core.services.PlaintextSignatureService;
import ru.hh.oauth.subscribe.core.services.SignatureService;

public class YammerApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "https://www.yammer.com/oauth/authorize?oauth_token=%s";

    @Override
    public String getRequestTokenEndpoint() {
        return "https://www.yammer.com/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.yammer.com/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }

    @Override
    public SignatureService getSignatureService() {
        return new PlaintextSignatureService();
    }
}
