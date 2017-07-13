package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.*;
import org.scribe.utils.OAuthEncoder;

/**
 * OAuth API for Flickr.
 * 
 * @author Darren Greaves
 * @see <a href="http://www.flickr.com/services/api/">Flickr API</a>
 */
public class VimeoApi20 extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://api.vimeo.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";
  private static final String SCOPED_AUTHORIZE_URL = String.format("%s&scope=%%s", AUTHORIZE_URL);

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://api.vimeo.com/oauth/access_token";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    if(config.hasScope())// Appending scope if present
    {
      return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
    }
    else
    {
      return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }
}
