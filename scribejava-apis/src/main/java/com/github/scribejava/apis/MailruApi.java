package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;
import com.github.scribejava.apis.service.MailruOAuthServiceImpl;
import com.github.scribejava.core.oauth.OAuth20Service;

public class MailruApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "https://connect.mail.ru/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

    protected MailruApi() {
    }

    private static class InstanceHolder {
        private static final MailruApi INSTANCE = new MailruApi();
    }

    public static MailruApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://connect.mail.ru/oauth/token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Valid url is required for a callback. Mail.ru does not support OOB");
        if (config.hasScope()) { // Appending scope if present
            return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()),
                    OAuthEncoder.encode(config.getScope()));
        } else {
            return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
        }
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new MailruOAuthServiceImpl(this, config);
    }
}
