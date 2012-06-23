package org.scribe.builder.api;

import org.scribe.model.*;

/**
 * OAuth API for XING
 *
 * @author <a href="https://www.xing.com/profile/Patrick_Alberts">Patrick Alberts</a>
 * @see <a href="https://dev.xing.com/">XING API</a>
 */
public class XingApi extends DefaultApi10a
{
	private static final String API_ENDPOINT = "https://api.xing.com/v1";
	private static final String REQUEST_TOKEN_RESOURCE = "/request_token";
	private static final String AUTHORIZE_URL = "/authorize?oauth_token=%s";
	private static final String ACCESS_TOKEN_RESOURCE = "/access_token";

	@Override
	public String getAccessTokenEndpoint()
	{
		return API_ENDPOINT + ACCESS_TOKEN_RESOURCE;
	}

	@Override
	public String getRequestTokenEndpoint()
	{
		return API_ENDPOINT + REQUEST_TOKEN_RESOURCE;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken)
	{
		return String.format(API_ENDPOINT + AUTHORIZE_URL, requestToken.getToken());
	}
}

