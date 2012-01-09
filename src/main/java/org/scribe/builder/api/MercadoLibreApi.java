package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.Preconditions;

import static org.scribe.utils.URLUtils.formURLEncode;

/**
 * @author: Pablo Molnar
 */
public class MercadoLibreApi extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://auth.mercadolibre.com.ar/authorization?response_type=code&client_id=%s&redirect_uri=%s";
  private static final String ACCESS_TOKEN_URL = "https://api.mercadolibre.com/oauth/token?grant_type=authorization_code";
  private static final String ACCEPT_MEDIA_TYPE = "application/json";


  @Override
  public String getAccessTokenEndpoint()
  {
    return ACCESS_TOKEN_URL;
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. MercadoLibre does not support OOB");
    return String.format(AUTHORIZE_URL, config.getApiKey(), formURLEncode(config.getCallback()));
  }

  @Override
  public Verb getAccessTokenVerb() {
    return Verb.POST;
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }

  @Override
  public String getAcceptMediaType() {
    return ACCEPT_MEDIA_TYPE;
  }
}
