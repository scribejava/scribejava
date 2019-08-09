package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

public class MeetupApi20 extends DefaultApi20 {

    protected MeetupApi20() {
    }

    private static class InstanceHolder {
        private static final MeetupApi20 INSTANCE = new MeetupApi20();
    }

    public static MeetupApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://secure.meetup.com/oauth2/access";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://secure.meetup.com/oauth2/authorize";
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }
}
