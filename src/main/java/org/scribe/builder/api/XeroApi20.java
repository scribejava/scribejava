package org.scribe.builder.api;

import org.scribe.model.*;

public class XeroApi20 extends DefaultApi10a{

	private static final String BASE_URL = "https://api.xero.com/oauth/";
	
	@Override
	public String getAccessTokenEndpoint() {
		return BASE_URL + "AccessToken";
	}

	@Override
	public String getRequestTokenEndpoint() {
		return BASE_URL + "RequestToken";
	}
	
	@Override
	public String getAuthorizationUrl(Token token) {
		return BASE_URL + "Authorize?oauth_token=" + token.getToken();
	}
}
