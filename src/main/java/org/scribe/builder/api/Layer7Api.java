package org.scribe.builder.api;

import java.io.IOException;
import java.util.Properties;

import org.scribe.model.*;

public class Layer7Api extends DefaultApi10a
{
  private static final String HOST = "preview.layer7tech.com";
  private static final String PROTOCOL = "http";
  private static final String PORT = "8080";
  private static final String PROTOCOL_SSL = "https";
  private static final String PORT_SSL = "8447";
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
    return String.format(AUTHORIZE_URL, PROTOCOL_SSL, HOST, PORT_SSL, token.getToken());
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return String.format(REQUEST_TOKEN_RESOURCE, PROTOCOL, HOST, PORT);
  }
  
  public static class SSL extends Layer7Api
  {
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
  
  public static class Custom extends Layer7Api
  {
    private final Properties prop = new Properties();
    private String host, method, port;
    
    /*
     * Loads the host, port, and method from the properties file 
     * the first time this method is run. 
     */
    private void readProperties()
    {
      if (null == host || null == port || null == method)
      {
          try
          {
            prop.load(Layer7Api20.class.getResourceAsStream("layer7.properties"));
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
          host = prop.getProperty("oauth1.hostname", "preview.layer7tech.com");
          port = prop.getProperty("oauth1.ssl.port", "8447");
          method = prop.getProperty("oauth.method", "https");
      }
    }
    
    @Override
    public String getAccessTokenEndpoint()
    {
      readProperties();
      return String.format(ACCESS_TOKEN_RESOURCE, method, host, port);
    }

    @Override
    public String getAuthorizationUrl(Token token)
    {
      readProperties();
      return String.format(AUTHORIZE_URL, method, host, port, token.getToken());
    }

    @Override
    public String getRequestTokenEndpoint()
    {
      readProperties();
      return String.format(REQUEST_TOKEN_RESOURCE, method, host, port);
    }
  }
}
