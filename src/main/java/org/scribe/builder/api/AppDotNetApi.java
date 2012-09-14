package org.scribe.builder.api;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class AppDotNetApi extends DefaultApi20 {
	private static final String AUTHORIZATION_URL = "https://alpha.app.net/oauth/authenticate?client_id=%s&response_type=token&redirect_uri=%s&scope=%s";

	@Override
	public String getAccessTokenEndpoint() {
		return "https://alpha.app.net/oauth/access_token?grant_type=authorization_code";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		return String.format(AUTHORIZATION_URL, config.getApiKey(),
				OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new JsonTokenExtractor();
	}
}
