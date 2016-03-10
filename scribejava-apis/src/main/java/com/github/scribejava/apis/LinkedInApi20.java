package com.github.scribejava.apis;

import com.github.scribejava.apis.service.LinkedIn20ServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

public class LinkedInApi20 extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=%s&redirect_uri=%s&"
            + OAuthConstants.STATE + "=%s";

    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

    protected LinkedInApi20() {
    }

    private static class InstanceHolder {
        private static final LinkedInApi20 INSTANCE = new LinkedInApi20();
    }

    public static LinkedInApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.linkedin.com/uas/oauth2/accessToken";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                "Must provide a valid url as callback. LinkedIn does not support OOB");

        if (config.hasScope()) {
            return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()),
                    config.getState(), OAuthEncoder.encode(config.getScope()));
        } else {
            return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()),
                    config.getState());
        }
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new LinkedIn20ServiceImpl(this, config);
    }
}
