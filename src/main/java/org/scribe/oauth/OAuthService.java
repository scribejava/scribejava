package org.scribe.oauth;

import java.util.Map;

import org.scribe.model.*;

/**
 * The main Scribe object. 
 * 
 * A facade responsible for the retrieval of request and access tokens and for the signing of HTTP requests.  
 * 
 * @author Pablo Fernandez
 */
public interface OAuthService
{
  /**
   * Retrieve the request token.
   * 
   * @return request token
   */
  public Token getRequestToken();

  /**
   * Retrieve the access token
   * 
   * @param requestToken request token (obtained previously)
   * @param verifier verifier code
   * @return access token
   */
  public Token getAccessToken(Token requestToken, Verifier verifier);
  
  /**
   * Retrieve the access token and parameters included in the response
   * 
   * @param requestToken request token (obtained previously)
   * @param verifier verifier code
   * @param responseParams any parameters included in the response are inserted
   *        into this map
   * @return access token
   */
  public Token getAccessToken(Token requestToken, Verifier verifier, Map<String, String> responseParams);

  /**
   * Signs am OAuth request
   * 
   * @param accessToken access token (obtained previously)
   * @param request request to sign
   */
  public void signRequest(Token accessToken, OAuthRequest request);

  /**
   * Returns the OAuth version of the service.
   * 
   * @return oauth version as string
   */
  public String getVersion();

  /**
   * Adds the 'scope' parameter. This is **not** a default OAuth parameter and the setting is optional.
   * 
   * @param scope OAuth Api scope (optional)
   */
  public void addScope(String scope);
  
  /**
   * Returns the URL where you should redirect your users to authenticate
   * your application.
   * 
   * @param requestToken the request token you need to authorize
   * @return the URL where you should redirect your users
   */
  public String getAuthorizationUrl(Token requestToken);
}
