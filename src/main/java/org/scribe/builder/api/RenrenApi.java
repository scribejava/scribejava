package org.scribe.builder.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.utils.Preconditions;
import org.scribe.utils.URLUtils;

public class RenrenApi extends DefaultApi20 {

	private static final String ACCESS_TOKEN_URL = "https://graph.renren.com/oauth/token?grant_type=authorization_code";
	private static final String AUTHORIZE_URL = "https://graph.renren.com/oauth/authorize?client_id=%s&response_type=code&redirect_uri=%s";

	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		return String.format(AUTHORIZE_URL, config.getApiKey(),
				config.getCallback());
	}

	public AccessTokenExtractor getAccessTokenExtractor() {
		return new AccessTokenExtractor() {

			private static final String TOKEN_REGEX = "^.*access_token\": \"(\\S*?)\".*$";
			private static final String EMPTY_SECRET = "";

			/**
			 * {@inheritDoc}
			 */
			public Token extract(String response) {
				Preconditions
						.checkEmptyString(response,
								"Response body is incorrect. Can't extract a token from an empty string");

				Matcher matcher = Pattern.compile(TOKEN_REGEX, Pattern.DOTALL)
						.matcher(response);
				if (matcher.matches()) {
					String token = URLUtils.formURLDecode(matcher.group(1));
					return new Token(token, EMPTY_SECRET, response);
				} else {
					throw new OAuthException(
							"Response body is incorrect. Can't extract a token from this: '"
									+ response + "'", null);
				}
			}

		};
	}
	
}