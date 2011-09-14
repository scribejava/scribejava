package org.scribe.oauth;

import java.util.*;

import org.scribe.builder.api.*;
import org.scribe.model.*;

/**
 * OAuth 1.0a implementation of {@link OAuthService}
 * 
 * @author Pablo Fernandez
 */
public class OAuth10aServiceImpl implements OAuthService
{
  private static final String VERSION = "1.0";

  private OAuthConfig config;
  private DefaultApi10a api;

  /**
   * Default constructor
   * 
   * @param api OAuth1.0a api information
   * @param config OAuth 1.0a configuration param object
   */
  public OAuth10aServiceImpl(DefaultApi10a api, OAuthConfig config)
  {
    this.api = api;
    this.config = config;
  }

  /**
   * {@inheritDoc}
   */
  public Token getRequestToken()
  {
    OAuthRequest request = new OAuthRequest(api.getRequestTokenVerb(), api.getRequestTokenEndpoint());
    request.addOAuthParameter(OAuthConstants.CALLBACK, config.getCallback());
    addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
    addSignature(request);
    Response response = request.send();
    return api.getRequestTokenExtractor().extract(response.getBody());
  }

  private void addOAuthParams(OAuthRequest request, Token token)
  {
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, api.getTimestampService().getTimestampInSeconds());
    request.addOAuthParameter(OAuthConstants.NONCE, api.getTimestampService().getNonce());
    request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, config.getApiKey());
    request.addOAuthParameter(OAuthConstants.SIGN_METHOD, api.getSignatureService().getSignatureMethod());
    request.addOAuthParameter(OAuthConstants.VERSION, getVersion());
    if(config.hasScope()) request.addOAuthParameter(OAuthConstants.SCOPE, config.getScope());
    request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, token));
  }

  /**
   * {@inheritDoc}
   */
  public Token getAccessToken(Token requestToken, Verifier verifier)
  {
    OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
    request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
    request.addOAuthParameter(OAuthConstants.VERIFIER, verifier.getValue());
    addOAuthParams(request, requestToken);
    addSignature(request);
    Response response = request.send();
    return api.getAccessTokenExtractor().extract(response.getBody());
  }

  /**
   * {@inheritDoc}
   */
  public void signRequest(Token token, OAuthRequest request)
  {
    request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
    addOAuthParams(request, token);
    addSignature(request);
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
  public String getAuthorizationUrl(Token requestToken)
  {
    return api.getAuthorizationUrl(requestToken);
  }
  
  private String getSignature(OAuthRequest request, Token token)
  {
    String baseString = api.getBaseStringExtractor().extract(request);
    return api.getSignatureService().getSignature(baseString, config.getApiSecret(), token.getSecret());
  }

  private void addSignature(OAuthRequest request)
  {
    switch (config.getSignatureType())
    {
      case Header:
        String oauthHeader = api.getHeaderExtractor().extract(request);
        request.addHeader(OAuthConstants.HEADER, oauthHeader);
        break;
      case QueryString:
        for (Map.Entry<String, String> entry : request.getOauthParameters().entrySet())
        {
          request.addQuerystringParameter(entry.getKey(), entry.getValue());
        }
        break;
    }
  }
}
