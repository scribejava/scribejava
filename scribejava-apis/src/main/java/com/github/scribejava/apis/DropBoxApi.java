package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

public class DropBoxApi extends DefaultApi10a {

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.dropbox.com/1/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return "https://www.dropbox.com/1/oauth/authorize?oauth_token=" + requestToken.getToken();
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://api.dropbox.com/1/oauth/request_token";
    }

}
