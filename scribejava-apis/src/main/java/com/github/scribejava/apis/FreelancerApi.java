package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;

public class FreelancerApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "http://www.freelancer.com/users/api-token/auth.php?oauth_token=%s";

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
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }

    public static class Sandbox extends FreelancerApi {

        private static final String SANDBOX_AUTHORIZATION_URL = "http://www.sandbox.freelancer.com/users/api-token/auth.php";

        @Override
        public String getRequestTokenEndpoint() {
            return "http://api.sandbox.freelancer.com/RequestRequestToken/requestRequestToken.xml";
        }

        @Override
        public String getAccessTokenEndpoint() {
            return "http://api.sandbox.freelancer.com/RequestAccessToken/requestAccessToken.xml?";
        }

        @Override
        public String getAuthorizationUrl(Token requestToken) {
            return String.format(SANDBOX_AUTHORIZATION_URL + "?oauth_token=%s", requestToken.getToken());
        }
    }
}
