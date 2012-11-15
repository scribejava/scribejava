package org.scribe.builder.api;

import org.scribe.model.Token;

/**
 * OAuth access to the Magento Store REST API.
 * For more information visit http://www.magentocommerce.com/wiki/doc/webservices-api/introduction_to_rest_api
 */
public class Magento17Api extends DefaultApi10a
{
	// NOTE as there is no central service for Magento, this does not suit Builder pattern. Must map to your store
	private final String BASE_URL = "http://magentohostname/magento/index.php/";


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