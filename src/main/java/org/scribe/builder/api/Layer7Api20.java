package org.scribe.builder.api;

import java.util.*;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.utils.*;

/**
 * Supports the Layer 7 OAuth Toolkit's OAuth 2.0 implementation. The Authorization Server can be set by 
 * creating a 'layer7.properties' file and setting the properties oauth2.authz.hostname, 
 * oauth2.authz.port, and oauth2.authz.method.
 * EG: 
 *  oauth2.authz.hostname=preview.layer7tech.com 
 *  oauth2.authz.port=8447 
 *  oauth2.authz.method=https 
 * 
 * The values given above are used by default. The file 'layer7.properties' must be in the classpath
 */
public class Layer7Api20 extends DefaultApi20
{
  private static final String DEFAULT_METHOD = "https";
  private static final String DEFAULT_PORT = "8447";
  private static final String DEFAULT_HOST = "preview.layer7tech.com";
  private static final String AUTHORIZE_URL = "%s://%s:%s/auth/oauth/v2/authorize?response_type=code";

  private static String host;
  private static String port;
  private static String method;

  @Override
  public String getAccessTokenEndpoint()
  {
    setHostname();
    return String.format("%s://%s:%s/auth/oauth/v2/token?grant_type=authorization_code", method, host, port);
  }

  @Override
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    setHostname();
    StringBuilder authUrl = new StringBuilder();
    authUrl.append(String.format(AUTHORIZE_URL, method, host, port));

    // Append scope if present
    if (config.hasScope())
    {
      authUrl.append("&scope=").append(OAuthEncoder.encode(config.getScope()));
    }

    // add redirect URI if callback isn't equal to 'oob'
    if (!config.getCallback().equalsIgnoreCase("oob"))
    {
      authUrl.append("&redirect_uri=").append(OAuthEncoder.encode(config.getCallback()));
    }

    authUrl.append("&client_id=").append(OAuthEncoder.encode(config.getApiKey()));
    return authUrl.toString();
  }

  /*
   * sets the host, port, and method from a properties file the first time this method is run.
   */
  private void setHostname()
  {
    if (null == host || null == port || null == method)
    {
      Properties prop = Layer7Api.loadProperties();
      host = prop.getProperty("oauth2.authz.hostname", Layer7Api20.DEFAULT_HOST);
      port = prop.getProperty("oauth2.authz.port", Layer7Api20.DEFAULT_PORT);
      method = prop.getProperty("oauth2.authz.method", Layer7Api20.DEFAULT_METHOD);
    }
  }
  
}
