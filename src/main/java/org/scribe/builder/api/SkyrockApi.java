package org.scribe.builder.api;

import org.scribe.model.*;

/**
 * OAuth API for Skyrock.
 *
 * @author Nicolas Quiénot
 * @see <a href="http://www.skyrock.com/developer/">Skyrock.com API</a>
 */
public class SkyrockApi extends DefaultApi10a
{
  private static final String API_ENDPOINT = "https://api.skyrock.com/v2";
  private static final String REQUEST_TOKEN_RESOURCE = "/oauth/initiate";
  private static final String AUTHORIZE_URL = "/oauth/authorize?oauth_token=%s";
  private static final String ACCESS_TOKEN_RESOURCE = "/oauth/token";
  
  @Override
  public String getAccessTokenEndpoint()
  {
    return API_ENDPOINT + ACCESS_TOKEN_RESOURCE;
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return API_ENDPOINT + REQUEST_TOKEN_RESOURCE;
  }
  
  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(API_ENDPOINT + AUTHORIZE_URL, requestToken.getToken());
  }
}
