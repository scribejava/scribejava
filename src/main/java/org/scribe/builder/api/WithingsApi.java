package org.scribe.builder.api;

import org.scribe.model.Token;
import org.scribe.model.Verb;

/**
 * OAuth API for Withings.
 *
 * @see <a href="http://www.withings.com/en/api/">Withings API</a>
 */
public class WithingsApi extends DefaultApi10a
{

  @Override
  public String getRequestTokenEndpoint()
  {
    return "https://oauth.withings.com/account/request_token";
  }

  @Override
  public Verb getRequestTokenVerb() {
    return Verb.GET;
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return "https://oauth.withings.com/account/authorize?oauth_token=" + requestToken.getToken();
  }

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://oauth.withings.com/account/access_token";
  }
}
