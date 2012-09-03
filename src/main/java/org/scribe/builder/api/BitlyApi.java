package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class BitlyApi extends DefaultApi20 {

  private static final String AUTHORIZE_URL = "https://bitly.com/oauth/authorize?client_id=%s&redirect_uri=%s";

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://api-ssl.bitly.com/oauth/access_token";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Bitly does not support OOB");
    return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
  }

  @Override
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }
}
