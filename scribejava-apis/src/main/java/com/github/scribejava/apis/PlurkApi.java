package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

public class PlurkApi extends DefaultApi10a {

    private static final String REQUEST_TOKEN_URL = "http://www.plurk.com/OAuth/request_token";
    private static final String AUTHORIZATION_URL = "http://www.plurk.com/OAuth/authorize?oauth_token=%s";
    private static final String ACCESS_TOKEN_URL = "http://www.plurk.com/OAuth/access_token";

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

    public static class Mobile extends PlurkApi {

        private static final String AUTHORIZATION_URL = "http://www.plurk.com/m/authorize?oauth_token=%s";

        @Override
        public String getAuthorizationUrl(Token requestToken) {
            return String.format(AUTHORIZATION_URL, requestToken.getToken());
        }
    }
}
