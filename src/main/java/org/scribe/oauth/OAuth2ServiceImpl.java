package org.scribe.oauth;

import org.scribe.builder.api.*;
import org.scribe.model.*;

public class OAuth2ServiceImpl implements OAuthService
{
  private final Api api;
  private final OAuthConfig config;
  
  public OAuth2ServiceImpl(Api api, OAuthConfig config)
  {
    this.api = api;
    this.config = config;
  }

  @Override
  public void addScope(String scope)
  {
    throw new UnsupportedOperationException("OAuth 2 does not use scopes");
  }

  @Override
  public Token getAccessToken(Token requestToken, Verifier verifier)
  {
    throw new UnsupportedOperationException("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
  }

  @Override
  public Token getRequestToken()
  {
    throw new UnsupportedOperationException("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
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

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return null;
  }

}
