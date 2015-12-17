package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

public class ConstantContactApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://oauth.constantcontact.com/ws/oauth/confirm_access?oauth_token=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://oauth.constantcontact.com/ws/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://oauth.constantcontact.com/ws/oauth/request_token";
    }
}
