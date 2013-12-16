package org.scribe.builder.api;

import org.scribe.model.*;

/**
 * Odesk API.
 * @see <a href="http://developers.odesk.com/w/page/38106543/Authentication-using-OAuth">Odesk.com OAuth API</a>
 * @author Yegor Bugayenko (yegor@tpc2.com)
 */
public class OdeskApi extends DefaultApi10a
{
  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://www.odesk.com/api/auth/v1/oauth/token/access";
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return "https://www.odesk.com/services/api/auth?oauth_token=" + requestToken.getToken();
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "https://www.odesk.com/api/auth/v1/oauth/token/request";
  }
}
