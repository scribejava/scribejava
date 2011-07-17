package org.scribe.builder.api;

import org.scribe.model.Token;

public class PlurkApi extends DefaultApi10a {

    private static final String REQUEST_TOKEN_URL = "http://www.plurk.com/OAuth/request_token";
    private static final String AUTHORIZE_URL = "http://www.plurk.com/OAuth/authorize?oauth_token=%s";
    private static final String ACCESS_TOKEN_URL = "http://www.plurk.com/OAuth/access_token";

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

}
