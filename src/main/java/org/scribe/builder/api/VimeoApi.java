package org.scribe.builder.api;

public class VimeoApi extends DefaultApi10a
{
  @Override
  public String getAccessTokenEndpoint()
  {
    return "http://vimeo.com/oauth/access_token";
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "http://vimeo.com/oauth/request_token";
  }
}
