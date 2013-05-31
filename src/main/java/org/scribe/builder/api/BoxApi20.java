package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.extractors.RefreshTokenExtractor;
import org.scribe.extractors.RefreshTokenExtractor20Impl;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Verb;

public class BoxApi20
    extends DefaultApi20
{
    private static final String AUTHORIZATION_URL =
        "https://www.box.com/api/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s";
    
    @Override
    public String getAuthorizationUrl( OAuthConfig config )
    {
        return String.format( AUTHORIZATION_URL, config.getApiKey(), config.getCallback(), config.getScope() );
    }

    @Override
    public String getAccessTokenEndpoint()
    {
        return "https://www.box.com/api/oauth2/token";
    }

    @Override
    public Verb getAccessTokenVerb()
    {
        return Verb.POST;
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor()
    {
        return new JsonTokenExtractor();
    }
    
    @Override
    public RefreshTokenExtractor getRefreshTokenExtractor()
    {
        return new RefreshTokenExtractor20Impl();
    } 
    
    @Override
    public String getTokenParameterName()
    {
        return OAuthConstants.ACCESS_TOKEN;
    }
    
    @Override
    public String getRefreshTokenParameterName()
    {
        return OAuthConstants.REFRESH_TOKEN;
    }
}