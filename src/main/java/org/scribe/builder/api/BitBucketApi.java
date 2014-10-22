package org.scribe.builder.api;

import org.scribe.model.Token;

public class BitBucketApi extends DefaultApi10a{

	// the following will help you find the docs for bitbucket oauth
	// https://confluence.atlassian.com/display/BITBUCKET/OAuth+on+Bitbucket
	
	private static final String AUTHORIZE_URL = "https://bitbucket.org/api/1.0/oauth/authenticate?oauth_token=%s";
	
	@Override
	public String getAccessTokenEndpoint() {
		return "https://bitbucket.org/!api/1.0/oauth/access_token";
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}

	@Override
	public String getRequestTokenEndpoint() {
		return "https://bitbucket.org/!api/1.0/oauth/request_token";
	}
}
