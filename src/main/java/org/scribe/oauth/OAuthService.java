package org.scribe.oauth;

import org.scribe.model.*;

public interface OAuthService
{
  public Token getRequestToken();

  public Token getAccessToken(Token requestToken, Verifier verifier);

  public void signRequest(Token accessToken, OAuthRequest request);

  public String getVersion();
}
