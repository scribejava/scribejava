package org.scribe.oauth;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;
import org.scribe.model.Verb;

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

      if (api.getAccessTokenVerb().equals(Verb.POST))
      {
        request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        if (config.hasScope()) request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
        if (config.hasGrantType()) request.addBodyParameter(OAuthConstants.GRANT_TYPE, config.getGrantType());
      }
      else
      {
        request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
        request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        if (config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
        if (config.hasGrantType()) request.addQuerystringParameter(OAuthConstants.GRANT_TYPE, config.getGrantType());
      }
    Response response = request.send();
    return api.getAccessTokenExtractor().extract(response.getBody());
  }

  /**
   * {@inheritDoc}
   */
  public Token refreshAccessToken(Token accessToken)
  {

    String accessTokenEndpoint = api.getAccessTokenEndpoint();
    if (accessTokenEndpoint.contains("?grant_type="))
    {
      // handle the ugly case where the grant_type parameter is already hardcoded in the constant url
      accessTokenEndpoint = accessTokenEndpoint.substring(0, accessTokenEndpoint.indexOf("?"));
    }
    OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), accessTokenEndpoint);
    if (api.getAccessTokenVerb().equals(Verb.POST))
    {
       request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
       request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
       request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
       request.addBodyParameter(OAuthConstants.GRANT_TYPE, api.getRefreshTokenParameterName());
       request.addBodyParameter(api.getRefreshTokenParameterName(), accessToken.getToken());
    }
    else
    {
       request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
       request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
       request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
       request.addQuerystringParameter(OAuthConstants.GRANT_TYPE, api.getRefreshTokenParameterName());
       request.addQuerystringParameter(api.getRefreshTokenParameterName(), accessToken.getToken());
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
    request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
  }

  /**
   * {@inheritDoc}
   */
  public String getAuthorizationUrl(Token requestToken)
  {
    return api.getAuthorizationUrl(config);
  }

}
