package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class PlurkApi extends DefaultApi10a {

    private static final String REQUEST_TOKEN_URL = "http://www.plurk.com/OAuth/request_token";
    private static final String AUTHORIZATION_URL = "http://www.plurk.com/OAuth/authorize?oauth_token=%s";
    private static final String ACCESS_TOKEN_URL = "http://www.plurk.com/OAuth/access_token";

    protected PlurkApi() {
    }

    private static class InstanceHolder {
        private static final PlurkApi INSTANCE = new PlurkApi();
    }

    public static PlurkApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

    public static class Mobile extends PlurkApi {

        private static final String AUTHORIZATION_URL = "http://www.plurk.com/m/authorize?oauth_token=%s";

        private Mobile() {
        }

        private static class InstanceHolder {
            private static final Mobile INSTANCE = new Mobile();
        }

        public static Mobile instance() {
            return InstanceHolder.INSTANCE;
        }

        @Override
        public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
            return String.format(AUTHORIZATION_URL, requestToken.getToken());
        }
    }
}
