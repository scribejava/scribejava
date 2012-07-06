package org.scribe.oauth;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;

/**
 * An asynchronous version of the Scribe service.
 *
 * A facade responsible for the retrieval of request and access tokens and for the signing of HTTP requests
 * via asynchronous calls.
 *
 * @author Brett Wooldridge
 */
public interface OAuthServiceAsync
{
  /**
   * Callback interface for getRequestToken() method.
   */
  public interface RequestTokenCallback
  {
    /**
     * Called when the request Token is available.
     *
     * @param requestToken the request token
     */
    public void onRequestToken(Token requestToken);

    /**
     * Called when there is an error handling the request.
     *
     * @param authException the exception that occurred during handling
     */
    public void onError(OAuthException authException);
  }

  /**
   * Callback interface for getAccessToken() method.
   */
  public interface AccessTokenCallback
  {
    /**
     * Called when the access Token is available.
     *
     * @param accessToken the access token
     */
    public void onAccessToken(Token accessToken);

    /**
     * Called when there is an error handling the request.
     *
     * @param authException the exception that occurred during handling
     */
    public void onError(OAuthException authException);
  }

  /**
   * Start the request to retrieve the request token.  The provided callback will be called with the Token
   * when it is available.
   *
   * @param callBack an implementation of RequestTokenCallback to be invoked with the Token
   */
  public void getRequestToken(RequestTokenCallback callBack);

  /**
   * Start the request to retrieve the access token.  The provided callback will be called with the Token
   * when it is available.
   *
   * @param requestToken request token (obtained previously)
   * @param verifier verifier code
   * @param callBack  an implementation of AccessTokenCallback to be invoked with the Token
   */
  public void getAccessToken(Token requestToken, Verifier verifier, AccessTokenCallback callBack);

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
   * Returns the URL where you should redirect your users to authenticate
   * your application.
   * 
   * @param requestToken the request token you need to authorize
   * @return the URL where you should redirect your users
   */
  public String getAuthorizationUrl(Token requestToken);
}


