package org.scribe.oauth;

import java.util.*;

import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.utils.*;

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
    config.log("obtaining request token from " + api.getRequestTokenEndpoint());
    OAuthRequest request = new OAuthRequest(api.getRequestTokenVerb(), api.getRequestTokenEndpoint());

    config.log("setting oauth_callback to " + config.getCallback());
    request.addOAuthParameter(OAuthConstants.CALLBACK, config.getCallback());
    addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
    appendSignature(request);

    config.log("sending request...");
    Response response = request.send();
    String body = response.getBody();

    config.log("response status code: " + response.getCode());
    config.log("response body: " + body);
    return api.getRequestTokenExtractor().extract(body);
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

    config.log("appended additional OAuth parameters: " + MapUtils.toString(request.getOauthParameters()));
  }

  /**
   * {@inheritDoc}
   */
  public Token getAccessToken(Token requestToken, Verifier verifier)
  {
    config.log("obtaining access token from " + api.getAccessTokenEndpoint());
    OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
    request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
    request.addOAuthParameter(OAuthConstants.VERIFIER, verifier.getValue());

    config.log("setting token to: " + requestToken + " and verifier to: " + verifier);
    addOAuthParams(request, requestToken);
    appendSignature(request);
    Response response = request.send();
    return api.getAccessTokenExtractor().extract(response.getBody());
  }

  /**
   * {@inheritDoc}
   */
  public void signRequest(Token token, OAuthRequest request)
  {
    config.log("signing request: " + request.getCompleteUrl());

    // Do not append the token if empty. This is for two legged OAuth calls.
    if (!token.isEmpty())
    {
      request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
    }
    config.log("setting token to: " + token);
    addOAuthParams(request, token);
    appendSignature(request);
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
    config.log("generating signature...");
    String baseString = api.getBaseStringExtractor().extract(request);
    String signature = api.getSignatureService().getSignature(baseString, config.getApiSecret(), token.getSecret());

    config.log("base string is: " + baseString);
    config.log("signature is: " + signature);
    return signature;
  }

  private void appendSignature(OAuthRequest request)
  {
    switch (config.getSignatureType())
    {
      case Header:
        config.log("using Http Header signature");

        String oauthHeader = api.getHeaderExtractor().extract(request);
        request.addHeader(OAuthConstants.HEADER, oauthHeader);
        break;
      case QueryString:
        config.log("using Querystring signature");

        for (Map.Entry<String, String> entry : request.getOauthParameters().entrySet())
        {
          request.addQuerystringParameter(entry.getKey(), entry.getValue());
        }
        break;
    }
  }
}
