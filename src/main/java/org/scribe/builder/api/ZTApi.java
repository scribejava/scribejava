package org.scribe.builder.api;

import org.scribe.model.Token;

public class ZTApi extends DefaultApi10a {

    @Override
    public String getRequestTokenEndpoint() {
        System.out.println("getRequestTokenEndpoint() -> http://www.stichtingrz.co/oauth1/request");
        return "http://www.stichtingrz.co/oauth1/request";
    }

    @Override
    public String getAccessTokenEndpoint() {
        System.out.println("getAccessTokenEndpoint() -> http://www.stichtingrz.co/oauth1/access");
        return "http://www.stichtingrz.co/oauth1/access";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        System.out.println("getAuthorizationUrl() -> http://www.stichtingrz.co/oauth1/authorize?oauth_token=" + requestToken.getToken());
        return "http://www.stichtingrz.co/oauth1/authorize?oauth_token=" + requestToken.getToken();
    }
}
