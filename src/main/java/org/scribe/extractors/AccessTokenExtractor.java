package org.scribe.extractors;

import org.scribe.model.OAuthToken;

/**
 * Simple command object that extracts a {@link OAuthToken} from a String
 * 
 * @author Pablo Fernandez
 */
public interface AccessTokenExtractor {
	/**
	 * Extracts the access token from the contents of an Http Response
	 * 
	 * @param response
	 *            the contents of the response
	 * @return OAuth access token
	 */
	public OAuthToken extract(String response);
}
