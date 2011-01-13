package org.scribe.builder.api;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class FacebookApi extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://graph.facebook.com/oauth/authorize?response_type=token&client_id=%s&redirect_uri=%s";
  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    if(OAuthConstants.OUT_OF_BAND.equals(config.getCallback()))
    	throw new OAuthException("Facebook does not support oob authentication.");
    return String.format(AUTHORIZE_URL, config.getApiKey(), URLUtils.percentEncode(config.getCallback()));
  }
}
