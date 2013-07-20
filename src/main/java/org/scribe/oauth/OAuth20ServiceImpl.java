package org.scribe.oauth;

import org.scribe.builder.api.*;
import org.scribe.model.*;

import java.util.concurrent.TimeUnit;

public class OAuth20ServiceImpl implements OAuthService
{
  private static final String VERSION = "2.0";
  
  private final DefaultApi20 api;
  private final OAuthConfig config;
  private RequestTuner[] serviceTuners;
  private RequestTuner defaultTuner;

  /**
   * Default constructor
   * 
   * @param api OAuth2.0 api information
   * @param config OAuth 2.0 configuration param object
   */
  public OAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config)
  {
    this(api, config, new TimeoutTuner(2, TimeUnit.SECONDS));
  }

  /**
   * Default constructor
   *
   * @param api OAuth2.0 api information
   * @param config OAuth 2.0 configuration param object
   */
  public OAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config, RequestTuner defaultTuner, RequestTuner... serviceTuner)
  {
    this.api = api;
    this.config = config;
    this.serviceTuners = serviceTuner;
    this.defaultTuner = defaultTuner;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Token getAccessToken(Token requestToken, Verifier verifier, RequestTuner... tuner)
  {
    OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
    switch (api.getAccessTokenVerb())
    {
      case GET:
        request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
        request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        if (config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
      break;

      default:
        request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        if (config.hasScope()) request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
    }
    Response response = request.send(RequestTuner.append(serviceTuners, tuner, defaultTuner));
    return api.getAccessTokenExtractor().extract(response.getBody());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Token getRequestToken(RequestTuner... tuner)
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
