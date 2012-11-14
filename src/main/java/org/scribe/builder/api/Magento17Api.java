package org.scribe.builder.api;

import org.scribe.model.Token;

/**
 * OAuth access to the Meetup.com API.
 * For more information visit http://www.meetup.com/api
 */
public class Magento17Api extends DefaultApi10a
{
	private static final String BASE_URL = "http://ec2-107-22-49-30.compute-1.amazonaws.com/magento/index.php/";

	@Override
	public String getRequestTokenEndpoint() {
		return BASE_URL + "oauth/initiate";
	}

	@Override
	public String getAccessTokenEndpoint() {
		return BASE_URL + "oauth/token";
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return BASE_URL + "admin/oauth_authorize?oauth_token=" + requestToken.getToken(); //this implementation is for admin roles only...
	}
}
