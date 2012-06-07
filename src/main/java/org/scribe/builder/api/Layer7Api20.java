package org.scribe.builder.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class Layer7Api20 extends DefaultApi20
{
  private final Properties prop = new Properties();
  private String host;
  private String port;
  private String method;
  private final static String AUTHORIZE_URL = "%s://%s:%s/auth/oauth/v2/authorize?response_type=code";

  @Override
  public String getAccessTokenEndpoint()
  {
    readProperties();
    return String.format("%s://%s:%s/auth/oauth/v2/token?grant_type=authorization_code", method, host, port);
  }

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
        host = prop.getProperty("oauth2.authz.hostname", "preview.layer7tech.com");
        port = prop.getProperty("oauth2.authz.port", "8447");
        method = prop.getProperty("oauth2.authz.method", "https");
    }
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
    readProperties();
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
}
