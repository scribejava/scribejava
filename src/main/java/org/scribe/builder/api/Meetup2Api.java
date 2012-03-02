package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class Meetup2Api extends DefaultApi20
{
	private static final String AUTHORIZATION_URL = "https://secure.meetup.com/oauth2/authorize?client_id=%s&response_type=code%s&redirect_uri=%s";
	private static final String ACCESS_URL = "https://secure.meetup.com/oauth2/access?grant_type=authorization_code";
	
	@Override
	public String getAccessTokenEndpoint()
	{
		return ACCESS_URL;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config)
	{
	    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback.");
	    return String.format(AUTHORIZATION_URL,
	    			config.getApiKey(),
	    			config.getScope() != null && !config.getScope().isEmpty() ? String.format("&scope=%s", config.getScope()) : "",
	    			OAuthEncoder.encode(config.getCallback()));
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
