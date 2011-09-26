package org.scribe.builder.api;

import org.scribe.model.Token;

public class TwitterApi extends DefaultApi10a
{
  private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize?oauth_token=%s";
  private static final String REQUEST_TOKEN_RESOURCE = "api.twitter.com/oauth/request_token";
  private static final String ACCESS_TOKEN_RESOURCE = "api.twitter.com/oauth/access_token";
  
  @Override
  public String getAccessTokenEndpoint()
  {
    return "http://" + ACCESS_TOKEN_RESOURCE;
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "http://" + REQUEST_TOKEN_RESOURCE;
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }

  public static class SSL extends TwitterApi
  {
    @Override
    public String getAccessTokenEndpoint()
    {
      return "https://" + ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getRequestTokenEndpoint()
    {
      return "https://" + REQUEST_TOKEN_RESOURCE;
    }
  }

  /**
   * Twitter 'friendlier' authorization endpoint for OAuth.
   *
   * Uses SSL.
   */
  public static class Authenticate extends SSL
  {
    private static final String AUTHENTICATE_URL = "https://api.twitter.com/oauth/authenticate?oauth_token=%s";

    @Override
    public String getAuthorizationUrl(Token requestToken)
    {
      return String.format(AUTHENTICATE_URL, requestToken.getToken());
    }
  }

  /**
   * Just an alias to the default (SSL) authorization endpoint.
   *
   * Need to include this for symmetry with 'Authenticate' only.
   */
  public static class Authorize extends SSL{}
}
