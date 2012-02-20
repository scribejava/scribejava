package org.scribe.builder.api;

import org.scribe.model.Token;
import org.scribe.model.Verb;

public class BistriApi
    extends DefaultApi10a
{
    private static final String AUTHORIZATION_URL = "http://localhost:8080/oauth/authorize?oauth_token=%s";

    @Override
    public String getRequestTokenEndpoint()
    {
        return "http://localhost:8080/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint()
    {
        return "http://localhost:8080/oauth/access_token";
    }

    @Override
    public Verb getAccessTokenVerb()
    {
        return Verb.GET;
    }

    @Override
    public Verb getRequestTokenVerb()
    {
        return Verb.GET;
    }

    @Override
    public String getAuthorizationUrl( Token requestToken )
    {
        return String.format( AUTHORIZATION_URL, requestToken.getToken() );
    }
}
