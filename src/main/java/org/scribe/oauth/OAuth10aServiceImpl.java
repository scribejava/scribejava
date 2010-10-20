package org.scribe.oauth;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.services.*;

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
  private SignatureService signatureService;
  private TimestampService timestampService;
  private BaseStringExtractor baseStringExtractor;
  private HeaderExtractor headerExtractor;
  private RequestTokenExtractor rtExtractor;
  private AccessTokenExtractor atExtractor;
  private String scope;

  /**
   * Default constructor
   * 
   * @param signatureService OAuth 1.0a signature service
   * @param timestampService OAuth 1.0a timestamp service
   * @param baseStringExtractor OAuth 1.0a base string extractor
   * @param headerExtractor OAuth 1.0a http header extractor
   * @param rtExtractor OAuth 1.0a request token extractor
   * @param atExtractor OAuth 1.0a access token extractor
   * @param config OAuth 1.0a configuration param object
   */
  public OAuth10aServiceImpl(SignatureService signatureService, TimestampService timestampService, BaseStringExtractor baseStringExtractor,
      HeaderExtractor headerExtractor, RequestTokenExtractor rtExtractor, AccessTokenExtractor atExtractor, OAuthConfig config)
  {
    this.signatureService = signatureService;
    this.timestampService = timestampService;
    this.baseStringExtractor = baseStringExtractor;
    this.headerExtractor = headerExtractor;
    this.rtExtractor = rtExtractor;
    this.atExtractor = atExtractor;
    this.config = config;
    this.scope = NO_SCOPE;
  }

  /**
   * {@inheritDoc}
   */
  public Token getRequestToken()
  {
    OAuthRequest request = new OAuthRequest(config.getRequestTokenVerb(), config.getRequestTokenEndpoint());
    addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
    addOAuthHeader(request);
    Response response = request.send();
    return rtExtractor.extract(response.getBody());
  }

  private void addOAuthParams(OAuthRequest request, Token token)
  {
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, timestampService.getTimestampInSeconds());
    request.addOAuthParameter(OAuthConstants.NONCE, timestampService.getNonce());
    request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, config.getApiKey());
    request.addOAuthParameter(OAuthConstants.SIGN_METHOD, signatureService.getSignatureMethod());
    request.addOAuthParameter(OAuthConstants.VERSION, getVersion());
    request.addOAuthParameter(OAuthConstants.CALLBACK, config.getCallback());
    if(scope != NO_SCOPE) request.addOAuthParameter(OAuthConstants.SCOPE, scope);
    request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, token));
  }

  /**
   * {@inheritDoc}
   */
  public Token getAccessToken(Token requestToken, Verifier verifier)
  {
    OAuthRequest request = new OAuthRequest(config.getAccessTokenVerb(), config.getAccessTokenEndpoint());
    request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
    request.addOAuthParameter(OAuthConstants.VERIFIER, verifier.getValue());
    addOAuthParams(request, requestToken);
    addOAuthHeader(request);
    Response response = request.send();
    return atExtractor.extract(response.getBody());
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
  
  private String getSignature(OAuthRequest request, Token token)
  {
    String baseString = baseStringExtractor.extract(request);
    return signatureService.getSignature(baseString, config.getApiSecret(), token.getSecret());
  }

  private void addOAuthHeader(OAuthRequest request)
  {
    String oauthHeader = headerExtractor.extract(request);
    request.addHeader(OAuthConstants.HEADER, oauthHeader);
  }
}
