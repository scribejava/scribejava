package org.scribe.builder.api;

public class GoogleApi extends DefaultApi10a
{
  @Override
  protected String getAccessTokenEndpoint()
  {
    return "https://www.google.com/accounts/OAuthGetAccessToken"; 
  }

  @Override
  protected String getRequestTokenEndpoint()
  {
    return "https://www.google.com/accounts/OAuthGetRequestToken";
  }
}
