package org.scribe.builder.api;

import org.scribe.model.Token;

/**
 * Fanfou OAuth 1.0 api
 */
public class FanfouApi extends DefaultApi10a {
	private static final String AUTHORIZE_URL = "http://fanfou.com/oauth/authorize?oauth_token=%s";
	private static final String REQUEST_TOKEN_URL = "http://fanfou.com/oauth/request_token";
	private static final String ACCESS_TOKEN_URL = "http://fanfou.com/oauth/access_token";

	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String getRequestTokenEndpoint() {
		return REQUEST_TOKEN_URL;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}

}
