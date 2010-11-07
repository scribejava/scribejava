package org.scribe.oauth;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

public class OAuth2ServiceImpl implements OAuthService
{

  @Override
  public void addScope(String scope)
  {
    throw new UnsupportedOperationException("OAuth 2 does not use scopes");
  }

  @Override
  public Token getAccessToken(Token requestToken, Verifier verifier)
  {
    return null;
  }

  @Override
  public Token getRequestToken()
  {
    throw new UnsupportedOperationException("OAuth 2 does not use request tokens. Use 'getAccessToken' directly");
  }

  @Override
  public String getVersion()
  {
    return "2.0";
  }

  @Override
  public void signRequest(Token accessToken, OAuthRequest request)
  {
    
  }

}
