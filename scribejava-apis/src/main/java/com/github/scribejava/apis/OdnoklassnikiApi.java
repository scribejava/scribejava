package com.github.scribejava.apis;

import com.github.scribejava.apis.service.OdnoklassnikiServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

public class OdnoklassnikiApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "http://www.odnoklassniki.ru/oauth/authorize?client_id=%s&response_type=code&redirect_uri=%s";
    private static final String SCOPED_AUTHORIZE_URL = String.format("%s&scope=%%s", AUTHORIZE_URL);

    protected OdnoklassnikiApi() {
    }

    private static class InstanceHolder {
        private static final OdnoklassnikiApi INSTANCE = new OdnoklassnikiApi();
    }

    public static OdnoklassnikiApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://api.odnoklassniki.ru/oauth/token.do";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Valid url is required for a callback. Odnoklassniki does not support OOB");
        if (config.hasScope()) {
            return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()),
                    OAuthEncoder.encode(config.getScope()));
        } else {
            return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
        }
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new OdnoklassnikiServiceImpl(this, config);
    }
}
