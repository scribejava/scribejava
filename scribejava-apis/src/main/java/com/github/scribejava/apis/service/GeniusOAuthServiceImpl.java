package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.oauth.OAuth20Service;

public class GeniusOAuthServiceImpl extends OAuth20Service {
	private static final String USER_AGENT = "user-agent";
	private static final String AGENT_NAME = "example_name";

	public GeniusOAuthServiceImpl(DefaultApi20 api, OAuthConfig config) {
		super(api, config);
	}
	
	@Override
	protected <T extends AbstractRequest> T createAccessTokenRequest(String code, T request) {
		String RESPONSE_TYPE = "response_type";
		
		final OAuthConfig config = getConfig();
		request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
		request.addParameter(OAuthConstants.CLIENT_SECRET,  config.getApiSecret());
		request.addParameter(OAuthConstants.CODE,  code);
		request.addParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
		request.addParameter(RESPONSE_TYPE, "code");
		request.addParameter(OAuthConstants.GRANT_TYPE,  OAuthConstants.AUTHORIZATION_CODE);		
		request.addHeader(USER_AGENT, AGENT_NAME);
		
		return request;
	}
	
	@Override 
	public void signRequest(OAuth2AccessToken accessToken, AbstractRequest request) {
		request.addHeader(USER_AGENT, AGENT_NAME);
		request.addHeader("Authorization", "Bearer " + accessToken.getAccessToken());
	}
}
