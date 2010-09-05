package org.scribe.oauth;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.services.*;

public class OAuth10aServiceImpl implements OAuthService
{

  private static final String VERSION = "1.0";

  private OAuthConfig config;
  private SignatureService signatureService;
  private TimestampService timestampService;
  private BaseStringExtractor baseStringExtractor;
  private HeaderExtractor headerExtractor;
  private RequestTokenExtractor rtExtractor;
  private AccessTokenExtractor atExtractor;

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
  }

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
    request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, token));
  }

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

  public void signRequest(Token token, OAuthRequest request)
  {
    request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
    addOAuthParams(request, token);
    addOAuthHeader(request);
  }

  public String getVersion()
  {
    return VERSION;
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
