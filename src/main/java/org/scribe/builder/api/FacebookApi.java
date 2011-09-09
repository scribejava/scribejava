package org.scribe.builder.api;

import org.scribe.model.*;

import org.scribe.utils.*;
import static org.scribe.utils.URLUtils.*;

public class FacebookApi extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s";
  private static final String SCOPED = "&scope=%s";
  private static final String STATE = "&state=%s";

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://graph.facebook.com/oauth/access_token";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");

    StringBuilder url = new StringBuilder(String.format(AUTHORIZE_URL, config.getApiKey(), formURLEncode(config.getCallback())));

    // Append scope if present
    if(config.hasScope())
    {
      url.append(String.format(SCOPED, formURLEncode(config.getScope())));
    }

    // Append state if present
    if(config.hasState())
    {
      url.append(String.format(STATE, formURLEncode(config.getState())));
    }

    return url.toString();
  }
}
