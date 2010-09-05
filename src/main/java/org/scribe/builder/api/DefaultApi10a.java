package org.scribe.builder.api;

import org.scribe.extractors.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Request.Verb;
import org.scribe.oauth.OAuth10aServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.services.*;

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
  
  public abstract String getRequestTokenEndpoint();
  public abstract String getAccessTokenEndpoint();
  
  @Override
  public OAuthService createService(String apiKey, String apiSecret, String callback)
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
