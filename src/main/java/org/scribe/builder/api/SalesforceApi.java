package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.Preconditions;

public class SalesforceApi extends DefaultApi20 {
	
	private static final String AUTHORIZE_PARAM = "https://login.salesforce.com/services/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s";

	@Override
	public String getAccessTokenEndpoint() {
		return "https://login.salesforce.com/services/oauth2/token";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback");
		return String.format(AUTHORIZE_PARAM , config.getApiKey(), config.getCallback()  );
	}
	
	/**
	   * Returns the verb for the access token endpoint (defaults to GET)
	   * 
	   * @return access token endpoint verb
	 */
	@Override
	public Verb getAccessTokenVerb()
	{
	    return Verb.POST;
	}	

}
