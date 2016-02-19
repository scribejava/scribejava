package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class ConstantContactApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL
            = "https://oauth.constantcontact.com/ws/oauth/confirm_access?oauth_token=%s";

    protected ConstantContactApi() {
    }

    private static class InstanceHolder {
        private static final ConstantContactApi INSTANCE = new ConstantContactApi();
    }

    public static ConstantContactApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://oauth.constantcontact.com/ws/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://oauth.constantcontact.com/ws/oauth/request_token";
    }
}
