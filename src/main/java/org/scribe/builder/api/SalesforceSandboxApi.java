package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.utils.Preconditions;

public class SalesforceSandboxApi extends SalesforceApi {
	
	private static final String AUTHORIZE_PARAM = "https://test.salesforce.com/services/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s";
	
	@Override
	public String getAccessTokenEndpoint() 
	{
		return "https://test.salesforce.com/services/oauth2/token";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) 
	{
		Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback");
		return String.format(AUTHORIZE_PARAM , config.getApiKey(), config.getCallback()  );
	}
	
}
