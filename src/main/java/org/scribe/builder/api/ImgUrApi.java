package org.scribe.builder.api;

import org.scribe.model.*;

/**
 * OAuth API for ImgUr
 *
 * @author David Wursteisen
 * @see <a href="http://api.imgur.com/#authapi">ImgUr API</a>
 */
public class ImgUrApi extends DefaultApi10a
{

  @Override
  public String getRequestTokenEndpoint()
  {
    return "https://api.imgur.com/oauth/request_token";
  }

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://api.imgur.com/oauth/access_token";
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format("https://api.imgur.com/oauth/authorize?oauth_token=%s", requestToken.getToken());
  }
}

