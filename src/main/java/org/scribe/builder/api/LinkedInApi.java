package org.scribe.builder.api;

import org.scribe.model.*;

public class LinkedInApi extends DefaultApi10a
{
  private static final String AUTHORIZE_URL = "https://api.linkedin.com/uas/oauth/authenticate?oauth_token=%s";
  
  private static String permissions = "";
  
  public static void addScopePermision(String permission)
  {          
      if(!permissions.contains(permission))
      {
          if(permissions != "")
              permissions += "+";
            permissions += permission;
      }
  }
  
  public static void clearScopePermisions()
  {
      permissions = "";
  }

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://api.linkedin.com/uas/oauth/accessToken";
  }

  @Override
  public String getRequestTokenEndpoint()
  { 
    return "https://api.linkedin.com/uas/oauth/requestToken?scope=" + permissions;
  }
  
  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }
  
}
