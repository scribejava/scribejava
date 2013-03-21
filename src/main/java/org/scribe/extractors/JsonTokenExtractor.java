package org.scribe.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthToken;
import org.scribe.utils.Preconditions;

public class JsonTokenExtractor implements AccessTokenExtractor {
	private Pattern accessTokenPattern = Pattern
			.compile("\"access_token\":\\s*\"(\\S*?)\"");

	public OAuthToken extract(String response) {
		Preconditions.checkEmptyString(response,
				"Cannot extract a token from a null or empty String");
		Matcher matcher = accessTokenPattern.matcher(response);
		if (matcher.find()) {
			return new OAuthToken(matcher.group(1), "", response);
		} else {
			throw new OAuthException(
					"Cannot extract an acces token. Response was: " + response);
		}
	}

}