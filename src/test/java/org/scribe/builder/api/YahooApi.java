package org.scribe.builder.api;

import org.scribe.model.Token;

public class YahooApi extends DefaultApi10a
{
  private static final String AUTHORIZE_URL = "https://api.login.yahoo.com/oauth/v2/request_auth?oauth_token=%s";
  
  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://api.login.yahoo.com/oauth/v2/get_token";
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "https://api.login.yahoo.com/oauth/v2/get_request_token";
  }
  
  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }
}
