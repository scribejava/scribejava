package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.ClientAuthenticationType;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.OAuth2SignatureType;

/**
 * Wunderlist.com Api
 */
public class WunderlistAPI20 extends DefaultApi20 {


    protected WunderlistAPI20() {
    }

    private static class InstanceHolder {
        private static final WunderlistAPI20 INSTANCE = new WunderlistAPI20();
    }

    public static WunderlistAPI20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.wunderlist.com/oauth/access_token";  
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://www.wunderlist.com/oauth/authorize";
    }

    @Override
    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_URI_QUERY_PARAMETER;
    }
    
    @Override
    public ClientAuthenticationType getClientAuthenticationType() {
        return ClientAuthenticationType.REQUEST_BODY;
    }
}
