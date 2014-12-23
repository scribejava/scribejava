package org.scribe.builder.api;

import org.scribe.model.Token;

/**
 * Implementation for the Withings API.
 *
 * Please note that Withing requires you to send the OAuth signature as a URL parameter and not a HTTP header.
 *
 * This can be set with {@link org.scribe.builder.ServiceBuilder#signatureType(org.scribe.model.SignatureType)}.
 */
public class WithingsApi extends DefaultApi10a
{
  private static final String BASE_URL = "https://oauth.withings.com/account";

  @Override
  public String getAccessTokenEndpoint()
  {
    return BASE_URL + "/access_token";
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return BASE_URL + "/request_token";
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return BASE_URL + "/authorize?oauth_token=" + requestToken.getToken();
  }

}
