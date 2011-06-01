package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.MailruTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.MailruOAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.Preconditions;
import org.scribe.utils.URLUtils;

/**
 * Implementation for mail.ru
 * http://api.mail.ru/docs/guides/oauth/sites/
 */
public class MailruApi extends DefaultApi20
{

  private static final String AUTHORIZE_URL = "https://connect.mail.ru/oauth/authorize?client_id=%s&response_type=code&redirect_uri=%s";
  private static final String SCOPED_AUTHORIZE_URL = String.format("%s&scope=%%s", AUTHORIZE_URL);

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://connect.mail.ru/oauth/token";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback.");
    if (config.hasScope()) // Appending scope if present
    {
      return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), URLUtils.formURLEncode(config.getCallback()), URLUtils.formURLEncode(config.getScope()));
    } else {
      // use "widget" scope as default
      return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), URLUtils.formURLEncode(config.getCallback()), "widget");
    }
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new MailruTokenExtractor();
  }

  @Override
  public OAuthService createService(OAuthConfig config)
  {
    return new MailruOAuth20ServiceImpl(this, config);
  }

  @Override
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }
}
