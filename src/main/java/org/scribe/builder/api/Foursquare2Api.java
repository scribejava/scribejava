package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class Foursquare2Api extends DefaultApi20 {
	private static final String AUTHORIZATION_URL = "https://foursquare.com/oauth2/authenticate?client_id=%s&response_type=%s&redirect_uri=%s";

	@Override
	public String getAccessTokenEndpoint(OAuthConfig config) {
		return "https://foursquare.com/oauth2/access_token?grant_type=authorization_code"
				+ config.getGrantType().getTypeValue();
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		Preconditions
				.checkValidUrl(config.getCallback(),
						"Must provide a valid url as callback. Foursquare2 does not support OOB");
		return String.format(AUTHORIZATION_URL, config.getApiKey(), config
				.getResponseType().getTypeValue(), OAuthEncoder.encode(config
				.getCallback()));
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new JsonTokenExtractor();
	}
}
