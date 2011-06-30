/**
 * 
 */
package org.scribe.oauth;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

/**
 * @author SuperWang
 *
 */
public class OAuth20aHttp4BasedServiceImpl implements OAuthService {

	  private static final String VERSION = "2.0";

	  private OAuthConfig config;
	  private DefaultApi10a api;

	  /**
	   * Default constructor
	   * 
	   * @param api OAuth1.0a api information
	   * @param config OAuth 1.0a configuration param object
	   */
	  public OAuth20aHttp4BasedServiceImpl(DefaultApi10a api, OAuthConfig config)  {
	    this.api = api;
	    this.config = config;
	  }

	/* (non-Javadoc)
	 * @see org.scribe.oauth.OAuthService#getRequestToken()
	 */
	@Override
	public Token getRequestToken() {
	    throw new UnsupportedOperationException("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
	}

	/* (non-Javadoc)
	 * @see org.scribe.oauth.OAuthService#getAccessToken(org.scribe.model.Token, org.scribe.model.Verifier)
	 */
	@Override
	public Token getAccessToken(Token requestToken, Verifier verifier) {
	    OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
	    request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
	    request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
	    request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
	    request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
	    if(config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
	    Response response = request.send();
	    return api.getAccessTokenExtractor().extract(response.getBody());
	}

	/* (non-Javadoc)
	 * @see org.scribe.oauth.OAuthService#signRequest(org.scribe.model.Token, org.scribe.model.OAuthRequest)
	 */
	@Override
	public void signRequest(Token accessToken, OAuthRequest request) {
	    request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
	}

	/* (non-Javadoc)
	 * @see org.scribe.oauth.OAuthService#getVersion()
	 */
	@Override
	public String getVersion() {
		return VERSION;
	}

	/* (non-Javadoc)
	 * @see org.scribe.oauth.OAuthService#getAuthorizationUrl(org.scribe.model.Token)
	 */
	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return this.api.getAuthorizationUrl(requestToken);
	}

}
