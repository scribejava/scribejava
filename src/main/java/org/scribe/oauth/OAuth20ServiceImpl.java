package org.scribe.oauth;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.OAuthToken;
import org.scribe.model.Response;
import org.scribe.model.Verifier;

public class OAuth20ServiceImpl implements OAuthService {
	private static final String VERSION = "2.0";
	private static final String BEARER = "Bearer ";// douban
	private static final String OAUTH = "OAuth2 "; // sina
	private static final String MAC="MAC "; // renren

	private final DefaultApi20 api;
	private final OAuthConfig config;

	/**
	 * Default constructor
	 * 
	 * @param api
	 *            OAuth2.0 api information
	 * @param config
	 *            OAuth 2.0 configuration param object
	 */
	public OAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
		this.api = api;
		this.config = config;
	}

	/**
	 * {@inheritDoc}
	 */
	public OAuthToken getAccessToken(OAuthToken requestToken, Verifier verifier) {
		OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(),
				api.getAccessTokenEndpoint());
		request.addQuerystringParameter(OAuthConstants.CLIENT_ID,
				config.getApiKey());
		request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET,
				config.getApiSecret());
		request.addQuerystringParameter(OAuthConstants.CODE,
				verifier.getValue());
		request.addQuerystringParameter(OAuthConstants.REDIRECT_URI,
				config.getCallback());
		if (config.hasScope())
			request.addQuerystringParameter(OAuthConstants.SCOPE,
					config.getScope());
		Response response = request.send();
		return api.getAccessTokenExtractor().extract(response.getBody());
	}

	/**
	 * {@inheritDoc}
	 */
	public OAuthToken getRequestToken() {
		throw new UnsupportedOperationException(
				"Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
	}

	/**
	 * {@inheritDoc}
	 */
	public String getVersion() {
		return VERSION;
	}

	/**
	 * {@inheritDoc}
	 */
	public void signRequest(OAuthToken token, OAuthRequest request) {
		config.log("signing request: " + request.getCompleteUrl());
		config.log("setting token to: " + token);
		if (!token.isEmpty()) {
			switch (config.getSignatureType()) {
			case HeaderBear:
				config.log("using Http Header Bearer signature");
				request.addHeader(OAuthConstants.HEADER,
						BEARER + token.getToken());
				break;
			case HeaderOAuth:
				config.log("using Http Header OAuth2 signature");
				request.addHeader(OAuthConstants.HEADER,
						OAUTH + token.getToken());
			case QueryString:
				config.log("using Querystring signature");
				request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN,
						token.getToken());
				break;
			default:
				break;
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public String getAuthorizationUrl(OAuthToken requestToken) {
		return api.getAuthorizationUrl(config);
	}

}
