package org.scribe.builder.api;

import org.scribe.model.Token;

public class SimpleGeoApi extends DefaultApi10a {

	@Override
	public String getRequestTokenEndpoint()
	{
		return "";
	}

	@Override
	public String getAccessTokenEndpoint()
	{
		return "";
	}

	@Override
	public String getAuthorizationUrl(Token requestToken)
	{
		return "";
	}

}
