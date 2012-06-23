package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;

public class ConstantContactApi2 extends DefaultApi20 
{
	
	private static final String AUTHORIZE_URL = "https://oauth2.constantcontact.com/oauth2/oauth/siteowner/authorize?client_id=%s&response_type=code&redirect_uri=%s";
	
	@Override
	public String getAccessTokenEndpoint() 
	{
		return "https://oauth2.constantcontact.com/oauth2/oauth/token";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) 
	{
		return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
	}
}
