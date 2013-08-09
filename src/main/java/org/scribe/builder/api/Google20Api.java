package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Verb;
import org.scribe.model.ParameterList;
import org.scribe.extractors.tokenExtractors.JsonTokenExtractor;
import org.scribe.extractors.tokenExtractors.AccessTokenExtractor;

/**
 * @author Aleksey Leshko
 */

public class Google20Api extends DefaultApi20 {
	private static final String CODE = "code";
	private static final String FORCE = "force";
	private static final String OFFLINE = "offline";
	private static final String GRANT_TYPE = "grant_type";
	private static final String ACCESS_TYPE = "access_type";
	private static final String RESPONSE_TYPE = "response_type";
	private static final String APPROVAL_PROMPT = "approval_prompt";
	private static final String AUTHORIZATION_CODE = "authorization_code";
	private static final String ACCESS_TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
	private static final String AUTHORIZATION_URL_CORE = "https://accounts.google.com/o/oauth2/auth";

	@Override
	public String getAuthorizationUrl(OAuthConfig config)
	{
		String defaultAuthorizationUrl = AUTHORIZATION_URL_CORE + "?";
		defaultAuthorizationUrl += "&" + OAuthConstants.REDIRECT_URI + "=" + config.getCallback();
		defaultAuthorizationUrl += "&" + OAuthConstants.CLIENT_ID + "=" + config.getApiKey();
		defaultAuthorizationUrl += "&" + OAuthConstants.SCOPE + "=" + config.getScope();
		defaultAuthorizationUrl += "&" + RESPONSE_TYPE + "=" + CODE;
		defaultAuthorizationUrl += "&" + APPROVAL_PROMPT + "=" + FORCE;
		defaultAuthorizationUrl += "&" + ACCESS_TYPE + "=" + OFFLINE;
		return defaultAuthorizationUrl;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_TOKEN_URL;
	}

	@Override
	public Verb getAccessTokenVerb()
	{
		return Verb.POST;
	}

	public AccessTokenExtractor getAccessTokenExtractor()
	{
		return new JsonTokenExtractor();
	}

	public ParameterList getParameterList()
	{
		ParameterList parameterList = new ParameterList();
		parameterList.add(GRANT_TYPE, AUTHORIZATION_CODE);
		return parameterList;
	}
}