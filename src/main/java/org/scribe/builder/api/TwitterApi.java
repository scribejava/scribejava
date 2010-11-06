package org.scribe.builder.api;

public class TwitterApi extends DefaultApi10a
{
  @Override
  protected String getAccessTokenEndpoint()
  {
    return "http://api.twitter.com/oauth/access_token";
  }

  @Override
  protected String getRequestTokenEndpoint()
  {
    return "http://api.twitter.com/oauth/request_token";
  }
}
