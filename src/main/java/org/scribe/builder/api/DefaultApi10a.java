package org.scribe.builder.api;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.oauth.*;
import org.scribe.services.*;

/**
 * Default implementation of the OAuth protocol, version 1.0a
 * 
 * This class is meant to be extended by concrete implementations of the API.
 * If your Api adheres to the 1.0a protocol correctly, you just need to extend 
 * this class and define the getters for your endpoints.
 * 
 * @author Pablo Fernandez
 *
 */
public abstract class DefaultApi10a implements Api
{
  
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new TokenExtractorImpl();
  }

  public BaseStringExtractor getBaseStringExtractor()
  {
    return new BaseStringExtractorImpl();
  }

  public HeaderExtractor getHeaderExtractor()
  {
    return new HeaderExtractorImpl();
  }

  public RequestTokenExtractor getRequestTokenExtractor()
  {
    return new TokenExtractorImpl();
  }

  public SignatureService getSignatureService()
  {
    return new HMACSha1SignatureService(); 
  }

  public TimestampService getTimestampService()
  {
    return new TimestampServiceImpl();
  }
  
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }
  
  public Verb getRequestTokenVerb()
  {
    return Verb.POST;
  }
  
  /**
   * Returns the URL that receives the request token requests.
   * 
   * @return request token URL
   */
  public abstract String getRequestTokenEndpoint();
  
  /**
   * Returns the URL that receives the access token requests.
   * 
   * @return access token URL
   */
  public abstract String getAccessTokenEndpoint();
  
  /**
   * Returns the {@link OAuthService} for this Api
   * 
   * @param apiKey Key
   * @param apiSecret Api Secret
   * @param callback OAuth callback (either URL or 'oob')
   * @param scope OAuth scope (optional) 
   */
  public OAuthService createService(OAuthConfig config, String scope)
  {
    OAuthService service = createService(config);
    service.addScope(scope);
    return service;
  }
  
  private OAuthService createService(OAuthConfig config)
  {
    return new OAuth10aServiceImpl(this, config);
  }
}
