package org.scribe.oauth;

import org.scribe.builder.api.*;
import org.scribe.model.*;

/**
 * OAuth 1.0a implementation of {@link OAuthService}
 * 
 * @author Pablo Fernandez
 */
public class OAuth10aServiceImpl implements OAuthService
{
  private static final String NO_SCOPE = null;
  private static final String VERSION = "1.0";

  private OAuthConfig config;
  private DefaultApi10a api;
  private String scope;

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
    this.scope = NO_SCOPE;
  }

  /**
   * {@inheritDoc}
   */
  public Token getRequestToken()
  {
    OAuthRequest request = new OAuthRequest(api.getRequestTokenVerb(), api.getRequestTokenEndpoint());
    addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
    request.addOAuthParameter(OAuthConstants.CALLBACK, config.getCallback());
    addOAuthHeader(request);
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
    if(scope != NO_SCOPE) request.addOAuthParameter(OAuthConstants.SCOPE, scope);
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
    addOAuthHeader(request);
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
    addOAuthHeader(request);
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
  public void addScope(String scope)
  {
    this.scope = scope;
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

  private void addOAuthHeader(OAuthRequest request)
  {
    String oauthHeader = api.getHeaderExtractor().extract(request);
    request.addHeader(OAuthConstants.HEADER, oauthHeader);
  }
}
