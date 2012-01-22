package org.scribe.builder.api;

import org.scribe.model.Token;

/**
 * OAuth access to the trello.com api.
 * <p/>
 * For more information visit http://trello.com/api
 *
 * @author Harald Wartig <hwartig@googlemail.com>
 */
public class TrelloApi extends DefaultApi10a
{

  private static final String AUTHORIZE_URL = "https://trello.com/1/OAuthAuthorizeToken?oauth_token=%s";

  @Override
  public String getRequestTokenEndpoint()
  {
    return "https://trello.com/1/OAuthGetRequestToken";
  }

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://trello.com/1/OAuthGetAccessToken";
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }
}

