package org.scribe.builder.api;

public class YahooApi extends DefaultApi10a
{
  @Override
  protected String getAccessTokenEndpoint()
  {
    return "https://api.login.yahoo.com/oauth/v2/get_token";
  }

  @Override
  protected String getRequestTokenEndpoint()
  {
    return "https://api.login.yahoo.com/oauth/v2/get_request_token";
  }
}
