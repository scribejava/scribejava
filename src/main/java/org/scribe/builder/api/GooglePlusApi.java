package org.scribe.builder.api;

import java.util.HashMap;
import java.util.Map;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;

public class GooglePlusApi extends DefaultApi20 {
	public static final String AUTH_URL="https://accounts.google.com/o/oauth2/auth?client_id=%s&response_type=code&redirect_uri=%s";
	private static final String SCOPED_AUTHORIZE_URL = AUTH_URL + "&scope=%s";
	@Override
	public String getAccessTokenEndpoint() {
		return "https://accounts.google.com/o/oauth2/token";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		if (config.hasScope())
		{
			return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
		}
    else
		{
			return String.format(AUTH_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
		}
	}

	@Override
	public Verb getAccessTokenVerb() {
		// TODO Auto-generated method stub
		return Verb.POST;
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		// TODO Auto-generated method stub
		return new JsonTokenExtractor();
	}

	@Override
	public Map<String, String> getAdditionalAccessTokenParameters() {
		// TODO Auto-generated method stub
		HashMap<String, String> values=new HashMap<String, String>();
		values.put("grant_type","authorization_code");
		return values;
	}

}
