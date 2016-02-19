package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class TwitterApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize?oauth_token=%s";
    private static final String REQUEST_TOKEN_RESOURCE = "api.twitter.com/oauth/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "api.twitter.com/oauth/access_token";

    protected TwitterApi() {
    }

    private static class InstanceHolder {
        private static final TwitterApi INSTANCE = new TwitterApi();
    }

    public static TwitterApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://" + ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://" + REQUEST_TOKEN_RESOURCE;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

    /**
     * Twitter 'friendlier' authorization endpoint for OAuth.
     *
     * Uses SSL.
     */
    public static class Authenticate extends TwitterApi {

        private static final String AUTHENTICATE_URL = "https://api.twitter.com/oauth/authenticate?oauth_token=%s";

        private Authenticate() {
        }

        private static class InstanceHolder {
            private static final Authenticate INSTANCE = new Authenticate();
        }

        public static Authenticate instance() {
            return InstanceHolder.INSTANCE;
        }

        @Override
        public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
            return String.format(AUTHENTICATE_URL, requestToken.getToken());
        }
    }
}
