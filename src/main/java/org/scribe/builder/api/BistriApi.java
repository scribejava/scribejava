package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;

public class BistriApi
    extends DefaultApi20
{
    private static final String AUTHORIZATION_URL =
        "http://dev.bistri.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s";

    @Override
    public String getAccessTokenEndpoint()
    {
        return "http://dev.bistri.com/oauth/access_token?grant_type=authorization_code";
    }

    @Override
    public String getAuthorizationUrl( OAuthConfig config )
    {
        return String.format( AUTHORIZATION_URL, config.getApiKey(), OAuthEncoder.encode( config.getCallback() ) );
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
    public String getRefreshTokenParameterName()
    {
        return "refresh_token";
    }
}
