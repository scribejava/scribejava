package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;

import static org.scribe.utils.OAuthEncoder.encode;

public class SinaWeiboApi20 extends DefaultApi20 {
    private static final String AUTHORIZE_URL = "https://api.weibo.com/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.weibo.com/oauth2/access_token?grant_type=authorization_code";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        // Append scope if present
        if (config.hasScope()) {
            return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), encode(config.getCallback()), encode(config.getScope()));
        } else {
            return String.format(AUTHORIZE_URL, config.getApiKey(), encode(config.getCallback()));
        }
    }
}