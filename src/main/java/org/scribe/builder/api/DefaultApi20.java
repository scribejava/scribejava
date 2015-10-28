package org.scribe.builder.api;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

/**
 * Default implementation of the OAuth protocol, version 2.0 (draft 11)
 *
 * This class is meant to be extended by concrete implementations of the API,
 * providing the endpoints and endpoint-http-verbs.
 *
 * If your Api adheres to the 2.0 (draft 11) protocol correctly, you just need to extend
 * this class and define the getters for your endpoints.
 *
 * If your Api does something a bit different, you can override the different
 * extractors or services, in order to fine-tune the process. Please read the
 * javadocs of the interfaces to get an idea of what to do.
 *
 * @author Diego Silveira
 *
 */
public abstract class DefaultApi20 implements Api
{

  /**
   * Returns the access token extractor.
   * 
   * @return access token extractor
   */
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new TokenExtractor20Impl();
  }
	
  /**
   * Returns the verb for the access token endpoint (defaults to GET)
   * 
   * @return access token endpoint verb
   */
  public Verb getAccessTokenVerb()
  {
    return Verb.GET;
  }
	
  /**
   * Returns the URL that receives the access token requests.
   * 
   * @return access token URL
   */
  public abstract String getAccessTokenEndpoint();
	
  /**
   * Returns the URL where you should redirect your users to authenticate
   * your application.
   *
   * @param config OAuth 2.0 configuration param object
   * @return the URL where you should redirect your users
   */
  public abstract String getAuthorizationUrl(OAuthConfig config);

  /**
   * {@inheritDoc}
   */
  public OAuthService createService(OAuthConfig config)
  {
    return new OAuth20ServiceImpl(this, config);
  }
  
  /**
   * Returns the type of authentication (plain in form or QueryString or Basic in Header)
   * Default is QueryString for backward compatibility
   */
  public ParameterType getClientAuthenticationType(){
	  return ParameterType.QueryString;
  }

  /**
   * Returns the type of parameters (form, QueryString or Header)
   * Default is QueryString for backward compatibility
   */
  public ParameterType getParameterType() {
	  return ParameterType.QueryString;
  }

  /**
   * Allows APIs to handle the request for free param injection
   * @param the request ready to be sent by the service
   * @return the request to be sent by the service
   */
  public OAuthRequest handleRequest(OAuthRequest request) {
	  return request;
  }

  /**
   * Allow APIs to hack OAuth2 standard (usefull for facebook wich does not respect the standard
   * @return QueryString => the access token is send as a QueryString in the URL
   */
  public String getSignatureType()
  {
    return null;
  }
	

}
