package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.Verb;

public class FreelancerApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "http://www.freelancer.com/users/api-token/auth.php?oauth_token=%s";

    protected FreelancerApi() {
    }

    private static class InstanceHolder {
        private static final FreelancerApi INSTANCE = new FreelancerApi();
    }

    public static FreelancerApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://api.freelancer.com/RequestAccessToken/requestAccessToken.xml?";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "http://api.freelancer.com/RequestRequestToken/requestRequestToken.xml";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public Verb getRequestTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }

    public static class Sandbox extends FreelancerApi {

        private static final String SANDBOX_AUTHORIZATION_URL
                = "http://www.sandbox.freelancer.com/users/api-token/auth.php";

        private Sandbox() {
        }

        private static class InstanceHolder {
            private static final Sandbox INSTANCE = new Sandbox();
        }

        public static Sandbox instance() {
            return InstanceHolder.INSTANCE;
        }

        @Override
        public String getRequestTokenEndpoint() {
            return "http://api.sandbox.freelancer.com/RequestRequestToken/requestRequestToken.xml";
        }

        @Override
        public String getAccessTokenEndpoint() {
            return "http://api.sandbox.freelancer.com/RequestAccessToken/requestAccessToken.xml?";
        }

        @Override
        public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
            return String.format(SANDBOX_AUTHORIZATION_URL + "?oauth_token=%s", requestToken.getToken());
        }
    }
}
