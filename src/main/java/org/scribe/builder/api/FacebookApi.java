package org.scribe.builder.api;

import org.scribe.model.*;

import org.scribe.utils.*;
import static org.scribe.utils.URLUtils.*;

public class FacebookApi extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s";
  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";
  private static final String GRANT_TYPE_PARAM = "&grant_type=%s";
  private static final String CLIENT_SECRET_PARAM = "&client_secret=%s";
  
  private static final String SCOPED_GRANT_TYPED_URL = SCOPED_AUTHORIZE_URL+GRANT_TYPE_PARAM;
  private static final String GRANT_TYPED_URL = AUTHORIZE_URL + GRANT_TYPE_PARAM;
  private static final String SCOPED_GRANT_TYPED_CLIENT_SECRET_URL = SCOPED_AUTHORIZE_URL+GRANT_TYPE_PARAM+CLIENT_SECRET_PARAM;
  private static final String GRANT_TYPED_CLIENT_SECRET_URL = AUTHORIZE_URL + GRANT_TYPE_PARAM+CLIENT_SECRET_PARAM;
 
  
  public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://graph.facebook.com/oauth/access_token";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");

    // Append scope if present
    if(config.hasScope() && !config.hasGrantType())
    {
      return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), formURLEncode(config.getCallback()), formURLEncode(config.getScope())); 
    }
    else if(config.hasScope() && config.hasGrantType())
    {
      if(GRANT_TYPE_CLIENT_CREDENTIALS.equals(config.getGrantType()))
      {
        return String.format(SCOPED_GRANT_TYPED_CLIENT_SECRET_URL, 
                             config.getApiKey(),formURLEncode(config.getCallback()), 
                             formURLEncode(config.getScope()),formURLEncode(config.getGrantType()),config.getApiSecret());	
      }
      return String.format(SCOPED_GRANT_TYPED_URL, config.getApiKey(),formURLEncode(config.getCallback()),
                           formURLEncode(config.getScope()),formURLEncode(config.getGrantType()));
    }
    else if(!config.hasScope() && config.hasGrantType())
    {
        if(GRANT_TYPE_CLIENT_CREDENTIALS.equals(config.getGrantType()))
        {
          return String.format(GRANT_TYPED_CLIENT_SECRET_URL, 
                               config.getApiKey(),formURLEncode(config.getCallback()), 
                               formURLEncode(config.getGrantType()),config.getApiSecret());	
        }
        return String.format(GRANT_TYPED_URL, config.getApiKey(),formURLEncode(config.getCallback()),
                             formURLEncode(config.getGrantType()));    	
    }
    else
    {
      return String.format(AUTHORIZE_URL, config.getApiKey(), formURLEncode(config.getCallback()));
    }
  }
}
