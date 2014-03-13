package org.scribe.builder.api;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;
import org.scribe.extractors.*;
import org.scribe.model.*;

public class LiveApi extends DefaultApi20 {
	private static final String AUTHORIZE_URL = "https://oauth.live.com/authorize";

	@Override
	public String getAccessTokenEndpoint()
	{
		return "https://oauth.live.com/token?grant_type=authorization_code";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config, String state)	{
        AuthUrlBuilder builder = new DefaultAuthUrlBuilder();

        builder.setEndpoint(AUTHORIZE_URL)
                .setClientId(config.getApiKey())
                .setRedirectUrl(config.getCallback())
                .setScope(config.getScope())
                .setState(state)
                .setResponseType("code");
        return builder.build();
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor()
	{
		return new JsonTokenExtractor();
	}
}