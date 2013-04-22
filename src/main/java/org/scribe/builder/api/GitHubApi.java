/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;

public class GitHubApi extends DefaultApi20 {

    private static final String AUTHORIZATION_URL = "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s";
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZATION_URL + "&scope=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://github.com/login/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        if(config.hasScope()) {
            return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()),
                    OAuthEncoder.encode(config.getScope()));
        }
        return String.format(AUTHORIZATION_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }
  
}