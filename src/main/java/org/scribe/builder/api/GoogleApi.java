package org.scribe.builder.api;

public class GoogleApi extends DefaultApi10a
{
  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://www.google.com/accounts/OAuthGetRequestToken"; 
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "https://www.google.com/accounts/OAuthGetAccessToken";
  }
}
