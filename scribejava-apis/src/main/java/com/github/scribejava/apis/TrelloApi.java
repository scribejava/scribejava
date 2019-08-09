package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class TrelloApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://trello.com/1/OAuthAuthorizeToken";

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
    public String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }

}
