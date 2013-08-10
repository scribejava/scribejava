package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

import org.scribe.extractors.JsonTokenExtractor;

public class ViadeoApi extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://secure.viadeo.com/oauth-provider/authorize2?client_id=%s&redirect_uri=%s&response_type=code";
  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }
  
  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://secure.viadeo.com/oauth-provider/access_token2?grant_type=authorization_code";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Viadeo does not support OOB");

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
