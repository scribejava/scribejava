package org.scribe.builder.api;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class PicplzApi extends DefaultApi20 
{
	private static final String AUTHORIZATION_URL = "https://picplz.com/oauth2/authenticate?client_id=%s&response_type=code&redirect_uri=%s";

	@Override
	public String getAccessTokenEndpoint() 
	{
		return "https://picplz.com/oauth2/access_token?grant_type=authorization_code";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) 
	{
		Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Picplz does not support OOB");
		return String.format(AUTHORIZATION_URL, config.getApiKey(), URLUtils.formURLEncode(config.getCallback()));
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() 
	{
		return new JsonTokenExtractor();
	}

}
