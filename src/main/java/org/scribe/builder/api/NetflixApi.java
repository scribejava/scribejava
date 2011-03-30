package org.scribe.builder.api;

import org.scribe.model.*;

public class NetflixApi extends DefaultApi10a 
{
  private static final String AUTHORIZE_URL = "https://api-user.netflix.com/oauth/login?oauth_token=%s&oauth_consumer_key=%s";

  @Override
  public String getAccessTokenEndpoint() 
  {
    return "http://api.netflix.com/oauth/access_token";
  }
  
  @Override
  public String getRequestTokenEndpoint() 
  {
    return "http://api.netflix.com/oauth/request_token";
  }

  @Override
  public String getAuthorizationUrl(Token requestToken) 
  {
    return getAuthorizationUrl(requestToken, " ");
  }
  
  @Override
  public String getAuthorizationUrl(Token requestToken, String consumerKey) 
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken(), consumerKey);
  }
  
}
