package org.scribe.builder.api;

import org.scribe.model.Token;

public class NetProspexApi extends DefaultApi10a
{
  private static final String REQUEST_TOKEN_URL = "https://api.netprospex.com/1.0/oauth/request-token";
  private static final String ACCESS_TOKEN_URL = "https://api.netprospex.com/1.0/oauth/access-token";
  private static final String AUTHORIZE_URL = "https://api.netprospex.com/1.0/oauth/authorize?oauth_token=%s";

  @Override
  public String getRequestTokenEndpoint()
  {
    return REQUEST_TOKEN_URL;
  }

  @Override
  public String getAccessTokenEndpoint()
  {
    return ACCESS_TOKEN_URL;
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }
}
