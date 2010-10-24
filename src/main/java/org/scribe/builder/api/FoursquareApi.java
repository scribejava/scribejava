package org.scribe.builder.api;

public class FoursquareApi extends DefaultApi10a
{
  @Override
  protected String getAccessTokenEndpoint()
  {
    return "http://foursquare.com/oauth/access_token";
  }

  @Override
  protected String getRequestTokenEndpoint()
  {
    return "http://foursquare.com/oauth/request_token";
  }
}
