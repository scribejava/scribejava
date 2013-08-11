package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;

public class RedditApi extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "https://ssl.reddit.com/api/v1/authorize?client_id=%s&redirect_uri=%s";
	private static final String ACCESS_URL = "https://ssl.reddit.com/api/v1/access_token";
	private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s&response_type=code&duration=%s&state=%s";

	//Reddit-specifc OAuth params
	public boolean isPermanent = false;
	public String state		   = null;
	
	public RedditApi(boolean isPermanent, String state){
		this.isPermanent = isPermanent;
		this.state 		 = state;
	}
	
	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_URL;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		//Append scope if present. Reddit permits multiple scopes if comma delimited.
		if (config.hasScope()) {
			return String.format(SCOPED_AUTHORIZE_URL,
					config.getApiKey(),
					OAuthEncoder.encode(config.getCallback()),
					OAuthEncoder.encode(config.getScope()),
					isPermanent ? "permanent" : "temporary",
					state);
					
		} else {
			return String.format(AUTHORIZE_URL, 
					config.getApiKey(),
					OAuthEncoder.encode(config.getCallback()));
		}
	}

}
