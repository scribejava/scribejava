package org.scribe.builder.api;

import org.scribe.model.*;
import org.scribe.utils.*;

public class FacebookApi extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://graph.facebook.com/oauth/authorize?response_type=token&client_id=%s&redirect_uri=%s";

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");
    return String.format(AUTHORIZE_URL, config.getApiKey(), URLUtils.formURLEncode(config.getCallback()));
  }
}
