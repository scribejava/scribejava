package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.YandexOAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;

public class YandexApi extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://oauth.yandex.ru/authorize?response_type=code&client_id=%s";

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://oauth.yandex.ru/token";
  }

  @Override
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    return String.format(AUTHORIZE_URL, config.getApiKey());
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }

  @Override
  public OAuthService createService(OAuthConfig config)
  {
    return new YandexOAuth20ServiceImpl(this, config);
  }
}
