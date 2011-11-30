package org.scribe.builder.api;

import static org.scribe.utils.URLUtils.formURLEncode;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.Preconditions;

public class Google2Api extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=%s&redirect_uri=%s&scope=%s";

	@Override
	public String getAccessTokenEndpoint() {
		return "https://accounts.google.com/o/oauth2/token?grant_type=authorization_code";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		Preconditions
				.checkValidUrl(config.getCallback(),
						"Must provide a valid url as callback. Google does not support OOB");
		Preconditions
				.checkEmptyString(config.getScope(),
						"Must provide a valid value as scope. Google does not support no scope");

		return String.format(AUTHORIZE_URL, config.getApiKey(),
				formURLEncode(config.getCallback()),
				formURLEncode(config.getScope()));
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new JsonTokenExtractor();
	}

}
