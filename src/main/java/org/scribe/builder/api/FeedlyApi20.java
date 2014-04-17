package org.scribe.builder.api;


import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;

public class FeedlyApi20 extends DefaultApi20
{
	private static Sandbox sandBox;
	private String AUTHORIZE_URL = "https://cloud.feedly.com/v3/auth/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=%s";
	private String ACCESS_TOKEN_URL = "https://cloud.feedly.com/v3/auth/token?grant_type=authorization_code";

	@Override
	public String getAccessTokenEndpoint()
	{
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config)
	{
		return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), config.getScope());
	}

	@Override
	public Verb getAccessTokenVerb()
	{
		return Verb.POST;
	}

	public static Sandbox getSandbox()
	{
		sandBox = new Sandbox();
		return sandBox;
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor()
	{
		
		return new JsonTokenExtractor();
	}


	public static class Sandbox extends FeedlyApi20
	{
		private static String AUTHORIZE_URL = "https://sandbox.feedly.com/v3/auth/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=%s";
		private static String ACCESS_TOKEN_URL = "https://sandbox.feedly.com/v3/auth/token?grant_type=authorization_code";

		@Override
		public String getAccessTokenEndpoint()
		{
			return ACCESS_TOKEN_URL;
		}

		@Override
		public String getAuthorizationUrl(OAuthConfig config)
		{
			return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), config.getScope());
		}

	}

}
