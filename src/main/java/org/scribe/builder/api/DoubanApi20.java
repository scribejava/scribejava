package org.scribe.builder.api;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.utils.*;

/**
 * Douban OAuth 2.0 api.
 */
public class DoubanApi20 extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://www.douban.com/service/auth2/auth?client_id=%s&redirect_uri=%s&response_type=code";
  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

  @Override
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new DoubanTokenExtractor();
  }

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://www.douban.com/service/auth2/token?grant_type=authorization_code";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    // Append scope if present
    if (config.hasScope())
    {
      return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
    }
    else
    {
      return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }
  }
}
