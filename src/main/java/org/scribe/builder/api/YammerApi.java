package org.scribe.builder.api;

public class YammerApi extends DefaultApi10a
{

	@Override
	public String getRequestTokenEndpoint()
  {
		return "https://www.yammer.com/oauth/request_token";
	}

	@Override
	public String getAccessTokenEndpoint()
  {
		return "https://www.yammer.com/oauth/access_token";
	}

}
