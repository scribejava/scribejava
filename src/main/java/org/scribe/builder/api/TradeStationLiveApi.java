package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 * @author John Jelinek IV
 */
public class TradeStationLiveApi extends DefaultApi20 {
	private static final String BASEURL = "https://api.tradestation.com/v2/";
	private static final String AUTHORIZE_URL = BASEURL + "authorize?client_id=%s&response_type=code&redirect_uri=%s";

	@Override public String getAccessTokenEndpoint() {
		return BASEURL + "security/authorize";
	}

	@Override public String getAuthorizationUrl(OAuthConfig config) {
		Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. TradeStation does not support OOB.");
		return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
	}
	
	@Override public Verb getAccessTokenVerb() {
		return Verb.POST;
	}
	
	@Override public AccessTokenExtractor getAccessTokenExtractor() {
		return new JsonTokenExtractor();
	}
}
