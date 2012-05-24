package org.scribe.builder.api;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class Layer7Api20 extends DefaultApi20
{
  private final static String AUTHORIZE_URL = "https://preview.layer7tech.com:8447/auth/oauth/v2/authorize?response_type=code";

  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://preview.layer7tech.com:8447/auth/oauth/v2/token?grant_type=authorization_code";
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
    StringBuilder authUrl = new StringBuilder();
    authUrl.append(AUTHORIZE_URL);

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
