package org.scribe.builder.api;

public class TwitterApi extends DefaultApi10a
{
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
}
