package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

/**
 * OAuth access to the Meetup.com API. For more information visit http://www.meetup.com/api
 */
public class MeetupApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "http://www.meetup.com/authenticate";

    protected MeetupApi() {
    }

    private static class InstanceHolder {
        private static final MeetupApi INSTANCE = new MeetupApi();
    }

    public static MeetupApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "http://api.meetup.com/oauth/request/";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://api.meetup.com/oauth/access/";
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }
}
