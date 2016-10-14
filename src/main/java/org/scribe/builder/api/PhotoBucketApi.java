package org.scribe.builder.api;

import org.scribe.model.Token;

/**
 * @author Sachin Handiekar
 */
public class PhotoBucketApi extends DefaultApi10a 
{

	private static final String AUTHORIZATION_URL = "http://photobucket.com/apilogin/login?oauth_token=%s";

	@Override
	public String getRequestTokenEndpoint() 
	{
		return "http://api.photobucket.com/login/request";
	}

	@Override
	public String getAccessTokenEndpoint() 
	{
		return "http://api.photobucket.com/login/access";
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) 
	{
		return String.format(AUTHORIZATION_URL, requestToken.getToken());
	}
	
}
