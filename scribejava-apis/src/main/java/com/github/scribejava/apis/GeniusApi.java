package com.github.scribejava.apis;

import com.github.scribejava.apis.service.GeniusOAuthServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;

public class GeniusApi extends DefaultApi20 {
	
	private static final String AUTHORIZE_URL =
			"https://api.genius.com/oauth/authorize?"
			+ "client_id=%s&"
			+ "redirect_uri=%s&"
			+ "scope=%s&"
			+ "state=%s&"
			+ "response_type=code";
	
	private static final String ACCESS_TOKEN_URL =
			"https://YOUR_REDIRECT_URI/?"
			+ "code=CODE&"
			+ "state=SOME_STATE_VALUE";
	
	private static final String TOKEN_ENDPOINT_URL = "https://api.genius.com/oauth/token";

	protected GeniusApi() {}
	
	private static class InstanceHolder {
		private static final GeniusApi INSTANCE = new GeniusApi();
	}
	
	public static GeniusApi instance() {
		return InstanceHolder.INSTANCE;
	}
	
	@Override
	public String getAccessTokenEndpoint() {
		return TOKEN_ENDPOINT_URL;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		System.out.print("Authorisation URL: ");
		
		// User must provide these 5 elements to the service builder		
		String url = String.format(AUTHORIZE_URL, 
				OAuthEncoder.encode(config.getApiKey()),
				OAuthEncoder.encode(config.getCallback()),
				OAuthEncoder.encode(config.getScope()),
				OAuthEncoder.encode(config.getState()));
		
		return url;
	}

	@Override
	public OAuth20Service createService(OAuthConfig config) {
		return new GeniusOAuthServiceImpl(this, config);	
	}
}
