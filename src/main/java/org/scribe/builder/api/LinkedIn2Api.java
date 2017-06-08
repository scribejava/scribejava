package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class LinkedIn2Api extends DefaultApi20 {
    public static final String  AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=%s&redirect_uri=%s";
    private static final String SCOPE             = "&scope=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.linkedin.com/uas/oauth2/accessToken?grant_type=authorization_code";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback.");

        String result = String.format(AUTHORIZATION_URL, config.getApiKey(),
            OAuthEncoder.encode(config.getCallback()));
        if (config.hasScope()) {
            return result + String.format(SCOPE, config.getScope());
        } else {
            return result;
        }
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

}
