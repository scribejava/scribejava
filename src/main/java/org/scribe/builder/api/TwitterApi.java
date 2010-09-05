package org.scribe.builder.api;

public class TwitterApi extends DefaultApi10a
{
  @Override
  public String getAccessTokenEndpoint()
  {
    return "http://twitter.com/oauth/access_token";
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "http://twitter.com/oauth/request_token";
  }
}
