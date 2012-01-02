package org.scribe.builder.api;

import org.scribe.model.*;

import org.scribe.utils.*;

public class FacebookApi extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s";
  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://graph.facebook.com/oauth/access_token";
  }
  
  /* Modified from client_credentials to null, because no session ID was 
   * received in the access_token, so https://graph.facebook.com/me gives back 
   * an OAuthException: "An active access token must be used to query 
   * information about the current user."
   * 
   * If you want to use this API for App Login, please override this method to 
   * return "client_credentials". 
   */
  @Override
  public String getGrantType() {
	return null;
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");

    // Append scope if present
    if(config.hasScope())
    {
     return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
    }
    else
    {
      return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }
  }
}
