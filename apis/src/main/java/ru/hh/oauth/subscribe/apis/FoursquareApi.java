package ru.hh.oauth.subscribe.apis;

import ru.hh.oauth.subscribe.core.builder.api.DefaultApi10a;
import ru.hh.oauth.subscribe.core.model.Token;

public class FoursquareApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "http://foursquare.com/oauth/authorize?oauth_token=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "http://foursquare.com/oauth/access_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "http://foursquare.com/oauth/request_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }
}
