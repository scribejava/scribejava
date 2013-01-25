package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.Preconditions;

public class ForceApi extends DefaultApi20 {
	
	public static final String TYPE = "authorization_code";
	
	private static final String AUTHORIZE_PARAM = "/services/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s";
	
	private static final String ACCESS_URL_PATH = "/services/oauth2/token";

	protected String baseURL = "https://login.salesforce.com";
	
	public static class Sandbox extends ForceApi
	{
		public Sandbox ()
		{
			baseURL = "https://test.salesforce.com";
		}
	}
	
	public static class PreRelease extends ForceApi
	{
		public PreRelease ()
		{
			baseURL = "https://prerellogin.pre.salesforce.com";
		}
	}
	
	@Override
	public String getAccessTokenEndpoint() {
		return baseURL + ACCESS_URL_PATH;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback");
		return String.format(baseURL + AUTHORIZE_PARAM , config.getApiKey(), config.getCallback()  );
	}
	
	  /**
	   * Returns the access token extractor.
	   * 
	   * @return access token extractor
	   */
	@Override
	public AccessTokenExtractor getAccessTokenExtractor()
	{
	    return new JsonTokenExtractor();
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
