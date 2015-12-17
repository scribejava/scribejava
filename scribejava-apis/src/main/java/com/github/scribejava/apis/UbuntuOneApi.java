package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.services.PlaintextSignatureService;
import com.github.scribejava.core.services.SignatureService;

/**
 * @author Julio Gutierrez
 *
 * Sep 6, 2012
 */
public class UbuntuOneApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "https://one.ubuntu.com/oauth/authorize/?oauth_token=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://one.ubuntu.com/oauth/access/";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://one.ubuntu.com/oauth/request/";
    }

    @Override
    public SignatureService getSignatureService() {
        return new PlaintextSignatureService();
    }

}
