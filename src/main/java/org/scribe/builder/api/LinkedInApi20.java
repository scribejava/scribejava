package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.SignatureType;
import org.scribe.model.Verb;

/**
 * @author Vladislav Zolotaryov
 */
public class LinkedInApi20 extends DefaultApi20
{

  private static final String AUTHORIZE_URL = "https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id={clientId}&redirect_uri={redirectUrl}&state={state}";

  private static final String SCOPED_AUTHORIZE_URL_PART = "&scope={scope}";

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://www.linkedin.com/uas/oauth2/accessToken";
  }

  private String formatAuthorizationUrl(OAuthConfig config)
  {
    String url = AUTHORIZE_URL;
    url = url.replace("{clientId}", config.getApiKey());
    url = url.replace("{redirectUrl}", config.getCallback());
    url = url.replace("{state}", config.getState());
    return url;
  }

  private String formatAuthorizationUrlWithScope(OAuthConfig config)
  {
    String url = formatAuthorizationUrl(config);
    url = url + SCOPED_AUTHORIZE_URL_PART;
    url = url.replace("{scope}", config.getScope());
    return url;
  }

  @Override
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    if (config.hasScope())
    {
      return formatAuthorizationUrlWithScope(config);
    }
    else
    {
      return formatAuthorizationUrl(config);
    }
  }

  @Override
  public boolean includeScope() {
    return false;
  }

  @Override
  public boolean includeGrantType() {
    return true;
  }

  @Override
  public SignatureType getSignatureType() {
    return SignatureType.Header;
  }

  @Override
  public String formatSignatureRequestToken(String token) {
    return OAuthConstants.BEARER + " " + token;
  }

  @Override
  public String getSignatureRequestKey() {
    return OAuthConstants.AUTHORIZATION_HEADER;
  }

}
