package org.scribe.builder.api;

import org.scribe.model.*;

/**
 * OAuth API for Flickr.
 * 
 * @author Darren Greaves
 * @see <a href="http://www.flickr.com/services/api/">Flickr API</a>
 */
public class FlickrApi extends DefaultApi10a
{

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAccessTokenEndpoint()
  {
    return "http://www.flickr.com/services/oauth/access_token";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return "http://www.flickr.com/services/oauth/authorize?oauth_token=" + requestToken.getToken();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRequestTokenEndpoint()
  {
    return "http://www.flickr.com/services/oauth/request_token";
  }
}
