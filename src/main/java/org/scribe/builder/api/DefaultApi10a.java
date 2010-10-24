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
  
  protected AccessTokenExtractor getAccessTokenExtractor()
  {
    return new TokenExtractorImpl();
  }

  protected BaseStringExtractor getBaseStringExtractor()
  {
    return new BaseStringExtractorImpl();
  }

  protected HeaderExtractor getHeaderExtractor()
  {
    return new HeaderExtractorImpl();
  }

  protected RequestTokenExtractor getRequestTokenExtractor()
  {
    return new TokenExtractorImpl();
  }

  protected SignatureService getSignatureService()
  {
    return new HMACSha1SignatureService(); 
  }

  protected TimestampService getTimestampService()
  {
    return new TimestampServiceImpl();
  }
  
  protected Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }
  
  protected Verb getRequestTokenVerb()
  {
    return Verb.POST;
  }
  
  /**
   * Returns the URL that receives the request token requests.
   * 
   * @return request token URL
   */
  protected abstract String getRequestTokenEndpoint();
  
  /**
   * Returns the URL that receives the access token requests.
   * 
   * @return access token URL
   */
  protected abstract String getAccessTokenEndpoint();
  
  /**
   * Returns the {@link OAuthService} for this Api
   * 
   * @param apiKey Key
   * @param apiSecret Api Secret
   * @param callback OAuth callback (either URL or 'oob')
   * @param scope OAuth scope (optional) 
   */
  public OAuthService createService(String apiKey, String apiSecret, String callback, String scope)
  {
    OAuthService service = createService(apiKey, apiSecret, callback);
    service.addScope(scope);
    return service;
  }
  
  private OAuthService createService(String apiKey, String apiSecret, String callback)
  {
    return new OAuth10aServiceImpl( getSignatureService(), 
                                    getTimestampService(), 
                                    getBaseStringExtractor(), 
                                    getHeaderExtractor(), 
                                    getRequestTokenExtractor(), 
                                    getAccessTokenExtractor(), 
                                    createConfig(apiKey, apiSecret, callback));
  }
  
  private OAuthConfig createConfig(String apiKey, String apiSecret, String callback)
  {
    OAuthConfig config = new OAuthConfig();
    config.setRequestTokenVerb(getRequestTokenVerb());
    config.setRequestTokenEndpoint(getRequestTokenEndpoint());
    config.setAccessTokenVerb(getAccessTokenVerb());
    config.setAccessTokenEndpoint(getAccessTokenEndpoint());
    config.setApiKey(apiKey);
    config.setApiSecret(apiSecret);
    config.setCallback(callback);
    return config;
  }

}
