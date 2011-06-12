package org.scribe.oauth;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;

public class YandexOAuth20ServiceImpl extends OAuth20ServiceImpl
{
  private final DefaultApi20 api;
  private final OAuthConfig config;

  public YandexOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config)
  {
    super(api, config);
    this.api = api;
    this.config = config;
  }

  @Override
  public Token getAccessToken(Token requestToken, Verifier verifier)
  {
    OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
    request.addBodyParameter("grant_type", "authorization_code");
    request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
    request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
    Response response = request.send();
    return api.getAccessTokenExtractor().extract(response.getBody());
  }

  @Override
  public void signRequest(Token accessToken, OAuthRequest request)
  {
    request.addQuerystringParameter("oauth_token", accessToken.getToken());
  }
}
