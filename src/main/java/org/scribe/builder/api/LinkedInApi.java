package org.scribe.builder.api;

public class LinkedInApi extends DefaultApi10a
{
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
}
