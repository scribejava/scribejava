package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public final class WordPressApi extends DefaultApi20
{
  private static final String BASE_URL = "https://public-api.wordpress.com/oauth2/";

  private static final String AUTHORIZE_URL = BASE_URL + "authorize?client_id=%s&redirect_uri=%s&response_type=code";

  @Override
  public String getAccessTokenEndpoint()
  {
    return BASE_URL + "token";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. WordPress does not support OOB");

    return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
  }

  @Override
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }

  @Override
  public Verb getAccessTokenParametersVerb()
  {
    return Verb.POST;
  }

  @Override
  public boolean isGrantTypeRequired()
  {
    return true;
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }
}
