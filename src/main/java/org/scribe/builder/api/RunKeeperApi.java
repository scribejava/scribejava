package org.scribe.builder.api;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class RunKeeperApi extends DefaultApi20{

    private static final String AUTHORIZATION_URL = "https://runkeeper.com/apps/authorize?response_type=code&redirect_uri=%s&client_id=%s";

    @Override
    public String getAccessTokenEndpoint() {
        // TODO Auto-generated method stub
        return "https://runkeeper.com/apps/token?grant_type=authorization_code";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkNotNull(config, "OAuthConfig can't be null");
        Preconditions.checkValidOAuthCallback(config.getCallback(), "Invalid OAuth Callback. Sorry ask Google for more info");
        return String.format(AUTHORIZATION_URL, OAuthEncoder.encode(config.getCallback()), config.getApiKey());
    }
    
    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
      return new JsonTokenExtractor();
    }
    
    @Override
    public Verb getAccessTokenVerb() {        
        return Verb.POST;
    }
    
    

}