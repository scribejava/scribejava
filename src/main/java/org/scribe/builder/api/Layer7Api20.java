package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;

public class Layer7Api20 extends DefaultApi20
{
  private final static String AUTHORIZE_URL = "https://cdryden-pc.l7tech.local:9443/auth/oauth/v2/authorize?response_type=code&client_id=%s&redirect_uri=%s";
  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";
  
  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://cdryden-pc.l7tech.local:9443/auth/oauth/v2/token?grant_type=authorization_code";
  }

  @Override
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }
  
  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }
  
  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {

    // Append scope if present
    if(config.hasScope())
    {
     return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
    }
    else
    {
      return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    } 
  }

}
