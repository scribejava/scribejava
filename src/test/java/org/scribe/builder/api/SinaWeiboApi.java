package org.scribe.builder.api;

import org.scribe.model.*;

public class SinaWeiboApi extends DefaultApi10a
{
	private static final String REQUEST_TOKEN_URL = "http://api.t.sina.com.cn/oauth/request_token";
	private static final String ACCESS_TOKEN_URL = "http://api.t.sina.com.cn/oauth/access_token";
	private static final String AUTHORIZE_URL = "http://api.t.sina.com.cn/oauth/authorize?oauth_token=%s";

	@Override
	public String getRequestTokenEndpoint()
	{
		return REQUEST_TOKEN_URL;
	}

	@Override
	public String getAccessTokenEndpoint()
	{
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken)
	{
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}
}
