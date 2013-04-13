package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class AsanaApi
    extends DefaultApi20
{
    private static final String AUTHORIZATION_URL = "https://app.asana.com/-/oauth_authorize?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String ACCESS_TOKEN_URL = "https://app.asana.com/-/oauth_token?grant_type=authorization_code";

    @Override
    public String getAccessTokenEndpoint()
    {
        return ACCESS_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl( OAuthConfig config )
    {
        Preconditions.checkValidUrl( config.getCallback(),
                                     "Must provide a valid url as callback. Asana does not support OOB" );

        return String.format( AUTHORIZATION_URL, config.getApiKey(), OAuthEncoder.encode( config.getCallback() ) );
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor()
    {
        return new JsonTokenExtractor();
    }

    @Override
    public Verb getAccessTokenVerb()
    {
        return Verb.POST;
    }
}