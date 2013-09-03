package org.scribe.builder.api;

import org.scribe.model.Token;

/**
 * Created with IntelliJ IDEA.
 * User: madkrupt
 * Date: 9/2/13
 * Time: 3:05 PM
 */
public class TripItApi extends DefaultApi10a {

    private final String AUTHORIZATION_URL="https://www.tripit.com/oauth/authorize?oauth_token=%s&oauth_callback=http://www.tripit.com";
    private final String BASE_URL="https://api.tripit.com/oauth/";

    @Override
    public String getRequestTokenEndpoint() {
        return BASE_URL + "request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return BASE_URL + "access_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }
}
