package org.scribe.oauth;

import org.scribe.model.OAuthRequest;
import org.scribe.model.OAuthToken;
import org.scribe.model.Verifier;

/**
 * The main Scribe object.
 * 
 * A facade responsible for the retrieval of request and access tokens and for
 * the signing of HTTP requests.
 * 
 * @author Pablo Fernandez
 */
public interface OAuthService {
	/**
	 * Retrieve the request token.
	 * 
	 * @return request token
	 */
	public OAuthToken getRequestToken();

	/**
	 * Retrieve the access token
	 * 
	 * @param requestToken
	 *            request token (obtained previously)
	 * @param verifier
	 *            verifier code
	 * @return access token
	 */
	public OAuthToken getAccessToken(OAuthToken requestToken, Verifier verifier);

	/**
	 * Signs am OAuth request
	 * 
	 * @param accessToken
	 *            access token (obtained previously)
	 * @param request
	 *            request to sign
	 */
	public void signRequest(OAuthToken accessToken, OAuthRequest request);

	/**
	 * Returns the OAuth version of the service.
	 * 
	 * @return oauth version as string
	 */
	public String getVersion();

	/**
	 * Returns the URL where you should redirect your users to authenticate your
	 * application.
	 * 
	 * @param requestToken
	 *            the request token you need to authorize
	 * @return the URL where you should redirect your users
	 */
	public String getAuthorizationUrl(OAuthToken requestToken);
}
