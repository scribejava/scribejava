package org.scribe.builder.api;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.FoursquareAccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.Preconditions;
import org.scribe.utils.URLUtils;

public class Foursquare2Api extends DefaultApi20 {

    @Override
    public String getAccessTokenEndpoint() {
        return "https://foursquare.com/oauth2/access_token?grant_type=authorization_code";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Foursquare2 does not support OOB");
        return String.format("https://foursquare.com/oauth2/authenticate?client_id=%s&response_type=code&redirect_uri=%s", config.getApiKey(), URLUtils.formURLEncode(config.getCallback()));
    }
    
    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new FoursquareAccessTokenExtractor();
    }
}
