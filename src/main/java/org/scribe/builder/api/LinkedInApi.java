package org.scribe.builder.api;

public class LinkedInApi extends DefaultApi10a
{
  private static final String AUTHORIZE_URL = "https://api.linkedin.com/uas/oauth/authorize?oauth_token=%s";

  @Override
  protected String getAccessTokenEndpoint()
  {
    return "https://api.linkedin.com/uas/oauth/accessToken";
  }

  @Override
  protected String getRequestTokenEndpoint()
  {
    return "https://api.linkedin.com/uas/oauth/requestToken";
  }
  
  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }
  
}
