package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

/**
 * OAuth API for Skyrock.
 *
 * @see <a href="http://www.skyrock.com/developer/">Skyrock.com API</a>
 */
public class SkyrockApi extends DefaultApi10a {

    private static final String API_ENDPOINT = "https://api.skyrock.com/v2";
    private static final String REQUEST_TOKEN_RESOURCE = "/oauth/initiate";
    private static final String AUTHORIZE_URL = "/oauth/authorize?oauth_token=%s";
    private static final String ACCESS_TOKEN_RESOURCE = "/oauth/token";

    protected SkyrockApi() {
    }

    private static class InstanceHolder {
        private static final SkyrockApi INSTANCE = new SkyrockApi();
    }

    public static SkyrockApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return API_ENDPOINT + ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return API_ENDPOINT + REQUEST_TOKEN_RESOURCE;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(API_ENDPOINT + AUTHORIZE_URL, requestToken.getToken());
    }
}
