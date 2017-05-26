package org.scribe.builder.api;

import java.io.*;
import java.util.*;

import org.scribe.exceptions.*;
import org.scribe.model.*;

/**
 * Supports the Layer 7 OAuth Toolkit's OAuth 1.0 implementation. The server can be set by 
 * adding the properties oauth1.hostname, oauth1.port, and oauth1.method to 'layer7.properties'
 * EG: 
 *  oauth1.hostname=preview.layer7tech.com 
 *  oauth1.port=8447 
 *  oauth1.method=https 
 * 
 * The values given above are used by default. The file 'layer7.properties' must be in the classpath
 * 
 */
public class Layer7Api extends DefaultApi10a
{
  private static final String AUTHORIZE_URL = "%s://%s:%s/auth/oauth/v1/authorize?oauth_token=%s";
  private static final String REQUEST_TOKEN_RESOURCE = "%s://%s:%s/auth/oauth/v1/request";
  private static final String ACCESS_TOKEN_RESOURCE = "%s://%s:%s/auth/oauth/v1/token";

  private static String host, method, port;

  @Override
  public String getAccessTokenEndpoint()
  {
    setHostname();
    return String.format(ACCESS_TOKEN_RESOURCE, method, host, port);
  }

  @Override
  public String getAuthorizationUrl(Token token)
  {
    setHostname();
    return String.format(AUTHORIZE_URL, method, host, port, token.getToken());
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    setHostname();
    return String.format(REQUEST_TOKEN_RESOURCE, method, host, port);
  }

  /*
   * Loads the host, port, and method from the properties file the first time this method is run.
   */
  private void setHostname()
  {
    if (null == host || null == port || null == method)
    {
      Properties prop = loadProperties();
      host = prop.getProperty("oauth1.hostname", "preview.layer7tech.com");
      port = prop.getProperty("oauth1.port", "8447");
      method = prop.getProperty("oauth1.method", "https");
    }
  }

  protected static Properties loadProperties()
  {
    final Properties prop = new Properties();
    try
    {
      final InputStream propertiesStream = Layer7Api.class.getResourceAsStream("/layer7.properties");
      if (propertiesStream != null)
        prop.load(propertiesStream);
    }
    catch (IOException e)
    {
      throw new OAuthException("Error while reading properties file", e);
    }
    return prop;
  }

}
