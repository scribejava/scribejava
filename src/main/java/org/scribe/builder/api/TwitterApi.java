package org.scribe.builder.api;

import org.scribe.model.Token;

public class TwitterApi extends DefaultApi10a
{
  private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize?oauth_token=%s";
  
  @Override
  public String getAccessTokenEndpoint()
  {
    return "http://api.twitter.com/oauth/access_token";
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "http://api.twitter.com/oauth/request_token";
  }
  
  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }
}
