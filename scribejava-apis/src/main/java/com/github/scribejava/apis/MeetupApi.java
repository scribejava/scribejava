package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

/**
 * OAuth access to the Meetup.com API. For more information visit http://www.meetup.com/api
 */
public class MeetupApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "http://www.meetup.com/authenticate?oauth_token=%s";

    @Override
    public String getRequestTokenEndpoint() {
        return "http://api.meetup.com/oauth/request/";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://api.meetup.com/oauth/access/";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}
