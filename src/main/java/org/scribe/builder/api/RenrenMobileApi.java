package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;

/**
 * 
 * @author daclouds
 * 
 * for Social Plugin v3.0.0Beta
 * http://wiki.mobile.renren.com/en/index.php
 *
 */
public class RenrenMobileApi extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "https://login.renren.com/mlogin/auth/auth?client_id=%s&redirect_uri=%s&response_type=code";
	private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new JsonTokenExtractor();
	}

	@Override
	public String getAccessTokenEndpoint() {
		return "https://login.renren.com/mlogin/auth/token?grant_type=authorization_code";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		// Append scope if present
		if (config.hasScope()) {
			return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(),
					OAuthEncoder.encode(config.getCallback()),
					OAuthEncoder.encode(config.getScope()));
		} else {
			return String.format(AUTHORIZE_URL, config.getApiKey(),
					OAuthEncoder.encode(config.getCallback()));
		}
	}

}
