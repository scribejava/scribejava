package org.scribe.oauth;

import java.net.Proxy;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.GrantType;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.OAuthToken;
import org.scribe.model.Response;
import org.scribe.model.ResponseType;
import org.scribe.model.SignatureType;
import org.scribe.model.Verifier;

public class OAuth20ServiceImpl implements OAuthService {
	private static final String VERSION = "2.0";
	private static final int CREDENTIALS_MIN_LENGTH = 3;
	private static final String CREDENTIALS_SEPARATOR = ":";

	private final DefaultApi20 api;
	private final OAuthConfig config;
	private java.net.Proxy proxy;

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
		if (config.getResponseType() == ResponseType.TOKEN) {
			return new OAuthToken(verifier.getValue(), "");
		}
		OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(),
				api.getAccessTokenEndpoint(config));
		GrantType type = config.getGrantType();

		if (type == GrantType.AUTHORIZATION_CODE) {
			request.addQuerystringParameter(OAuthConstants.CODE,
					verifier.getValue());
		} else if (type == GrantType.REFRESH_TOKEN) {
			request.addQuerystringParameter(OAuthConstants.REFRESH_TOKEN,
					verifier.getValue());
		} else if (type == GrantType.RESOURCE_OWNER_PASSWORD_CREDENTIALS) {
			String resource = verifier.getValue();
			if (resource != null
					&& resource.trim().length() >= CREDENTIALS_MIN_LENGTH) {
				String[] credentials = resource.split(CREDENTIALS_SEPARATOR);
				if (credentials != null && credentials.length == 2) {
					String userName = credentials[0];
					String password = credentials[1];
					request.addQuerystringParameter(OAuthConstants.USERNAME,
							userName);
					request.addQuerystringParameter(OAuthConstants.PASSWORD,
							password);
				}
			}
		}
		request.addQuerystringParameter(OAuthConstants.CLIENT_ID,
				config.getApiKey());
		request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET,
				config.getApiSecret());
		request.addQuerystringParameter(OAuthConstants.REDIRECT_URI,
				config.getCallback());
		request.addQuerystringParameter(OAuthConstants.GRANT_TYPE, config
				.getGrantType().getTypeValue());
		if (config.hasScope())
			request.addQuerystringParameter(OAuthConstants.SCOPE,
					config.getScope());

		config.log("setting proxy to " + proxy);
		if (proxy != null) {
			request.setProxy(proxy);
		}
		config.log("sending request...");
		Response response = request.send();
		String body = response.getBody();
		config.log("response status code: " + response.getCode());
		config.log("response body: " + body);
		return api.getAccessTokenExtractor().extract(body);
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
			case HEADER_BEARER:
				config.log("using Http Header Bearer signature");
				request.addHeader(OAuthConstants.HEADER,
						SignatureType.HEADER_BEARER.getTypeValue() + " "
								+ token.getToken());
				break;
			case HEADER_OAUTH:
				config.log("using Http Header OAuth2 signature");
				request.addHeader(
						OAuthConstants.HEADER,
						SignatureType.HEADER_OAUTH.getTypeValue() + " "
								+ token.getToken());
			case QUERY_STRING:
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

	@Override
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

}
