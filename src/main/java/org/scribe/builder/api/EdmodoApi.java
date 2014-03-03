package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class EdmodoApi extends DefaultApi20 {
	private static final String AUTHORIZE_URL = "https://api.edmodo.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";
	private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL
			+ "&scope=%s";

	@Override
	public String getAccessTokenEndpoint() {
		return "https://api.edmodo.com/oauth/token";
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}

	@Override
	public OAuthService createService(final OAuthConfig config) {
		return new OAuth20ServiceImpl(this, config) {
			public Token getAccessToken(Token requestToken, Verifier verifier) {
				OAuthRequest request = new OAuthRequest(
						EdmodoApi.this.getAccessTokenVerb(),
						EdmodoApi.this.getAccessTokenEndpoint());
				request.addQuerystringParameter(OAuthConstants.CLIENT_ID,
						config.getApiKey());
				request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET,
						config.getApiSecret());
				request.addQuerystringParameter(OAuthConstants.CODE,
						verifier.getValue());
				request.addQuerystringParameter("grant_type",
						"authorization_code");
				request.addQuerystringParameter(OAuthConstants.REDIRECT_URI,
						config.getCallback());
				if (config.hasScope())
					request.addQuerystringParameter(OAuthConstants.SCOPE,
							config.getScope());
				Response response = request.send();
				return EdmodoApi.this.getAccessTokenExtractor().extract(
						response.getBody());
			}
		};
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new JsonTokenExtractor();
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		Preconditions.checkValidUrl(config.getCallback(),
				"Invalid callback url");

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
