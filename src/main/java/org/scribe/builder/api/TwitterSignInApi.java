package org.scribe.builder.api;

import org.scribe.model.Token;
import org.scribe.builder.api.TwitterApi;


public class TwitterSignInApi extends TwitterApi {
	private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authenticate?oauth_token=%s";

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}
}
