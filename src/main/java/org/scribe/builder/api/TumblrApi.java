package org.scribe.builder.api;

import org.scribe.model.Token;

public class TumblrApi extends DefaultApi10a
{
  private static final String AUTHORIZE_URL = "https://www.tumblr.com/oauth/authorize?oauth_token=%s";
  private static final String REQUEST_TOKEN_RESOURCE = "www.tumblr.com/oauth/request_token";
  private static final String ACCESS_TOKEN_RESOURCE = "www.tumblr.com/oauth/access_token";
  
  @Override
  public String getAccessTokenEndpoint()
  {
    return "http://" + ACCESS_TOKEN_RESOURCE;
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "http://" + REQUEST_TOKEN_RESOURCE;
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }
}
