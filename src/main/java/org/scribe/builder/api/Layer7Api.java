package org.scribe.builder.api;

import org.scribe.model.Token;

public class Layer7Api extends DefaultApi10a
{
  private static final String HOST = "cdryden-pc.l7tech.local";
  private static final String PROTOCOL = "http";
  private static final String PORT = "8080";
  private static final String AUTHORIZE_URL = "%s://%s:%s/auth/oauth/v1/authorize?oauth_token=%s";
  private static final String REQUEST_TOKEN_RESOURCE = "%s://%s:%s/auth/oauth/v1/request";
  private static final String ACCESS_TOKEN_RESOURCE = "%s://%s:%s/auth/oauth/v1/token";

  @Override
  public String getAccessTokenEndpoint()
  {
    return String.format(ACCESS_TOKEN_RESOURCE, PROTOCOL, HOST, PORT);
  }

  @Override
  public String getAuthorizationUrl(Token token)
  {
    return String.format(AUTHORIZE_URL, PROTOCOL, HOST, PORT, token.getToken());
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return String.format(REQUEST_TOKEN_RESOURCE, PROTOCOL, HOST, PORT);
  }
  
  public static class SSL extends Layer7Api
  {
    private static final String PROTOCOL_SSL = "https";
    private static final String PORT_SSL = "8443";
    
    @Override
    public String getAccessTokenEndpoint()
    {
      return String.format(ACCESS_TOKEN_RESOURCE, PROTOCOL_SSL, HOST, PORT_SSL);
    }

    @Override
    public String getAuthorizationUrl(Token token)
    {
      return String.format(AUTHORIZE_URL, PROTOCOL_SSL, HOST, PORT_SSL, token.getToken());
    }

    @Override
    public String getRequestTokenEndpoint()
    {
      return String.format(REQUEST_TOKEN_RESOURCE, PROTOCOL_SSL, HOST, PORT_SSL);
    }
  }
}
