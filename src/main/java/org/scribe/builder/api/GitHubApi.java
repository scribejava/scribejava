package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.TokenExtractor20Impl;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Verb;
import org.scribe.utils.URLUtils;

public class GitHubApi extends DefaultApi20 {

  private static final String AUTHORIZATION_URL = "https://github.com/login/oauth/authorize?client_id=%s";
  private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";

  @Override
  public String getAccessTokenEndpoint()
  {
    return ACCESS_TOKEN_URL;
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    // Callbacks in GitHub are optional and default to the callback used to configure your GitHub application.
    // That being said, if your service wasn't configured with a callback, don't use one in the request url as
    // GitHub does host verification and will fail otherwise.
    String callback = config.getCallback();
    if (callback != null && !callback.trim().equals("") && !callback.equals(OAuthConstants.OUT_OF_BAND))
    {
      return String.format(AUTHORIZATION_URL + "&redirect_uri=%s", config.getApiKey(), URLUtils.formURLEncode(config.getCallback()));
    }
    else
    {
      return String.format(AUTHORIZATION_URL, config.getApiKey());
    }
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new TokenExtractor20Impl();
  }

  @Override
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }

}
