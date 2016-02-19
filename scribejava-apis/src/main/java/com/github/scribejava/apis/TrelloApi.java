package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class TrelloApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://trello.com/1/OAuthAuthorizeToken?oauth_token=%s";

    protected TrelloApi() {
    }

    private static class InstanceHolder {
        private static final TrelloApi INSTANCE = new TrelloApi();
    }

    public static TrelloApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://trello.com/1/OAuthGetAccessToken";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://trello.com/1/OAuthGetRequestToken";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

}
