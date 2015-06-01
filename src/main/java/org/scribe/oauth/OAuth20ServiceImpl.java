package org.scribe.oauth;

import org.scribe.builder.api.*;
import org.scribe.model.*;

public class OAuth20ServiceImpl implements OAuthService
{

  private static final String VERSION = "2.0";

  private final DefaultApi20 api;

  private final OAuthConfig config;

  /**
   * Default constructor
   * 
   * @param api OAuth2.0 api information
   * @param config OAuth 2.0 configuration param object
   */
  public OAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config)
  {
    this.api = api;
    this.config = config;
  }

  /**
   * {@inheritDoc}
   */
  public Token getAccessToken(Token requestToken, Verifier verifier)
  {
    OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
    request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
    request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
    request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
    request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());

    if (api.includeGrantType())
    {
      request.addQuerystringParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);
    }
    if (api.includeScope())
    {
      if (config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
    }

    Response response = request.send();
    return api.getAccessTokenExtractor().extract(response.getBody());
  }

  /**
   * {@inheritDoc}
   */
  public Token getRequestToken()
  {
    throw new UnsupportedOperationException("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
  }

  /**
   * {@inheritDoc}
   */
  public String getVersion()
  {
    return VERSION;
  }

  /**
   * {@inheritDoc}
   */
  public void signRequest(Token accessToken, OAuthRequest request)
  {
    switch (api.getSignatureType())
    {
    case Header:
      request.addHeader(api.getSignatureRequestKey(),
          api.formatSignatureRequestToken(accessToken.getToken()));
      break;
    case QueryString:
      request.addQuerystringParameter(api.getSignatureRequestKey(),
          api.formatSignatureRequestToken(accessToken.getToken()));
      break;
    }
  }

  /**
   * {@inheritDoc}
   */
  public String getAuthorizationUrl(Token requestToken)
  {
    return api.getAuthorizationUrl(config);
  }

}
