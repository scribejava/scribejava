package org.scribe.oauth;

import org.apache.commons.codec.binary.Base64;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

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
    request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
    request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
    if (config.hasScope())
      request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
    appendAuthentication(request);
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

  private void appendAuthentication(OAuthRequest request)
  {
    switch (config.getSignatureType())
    {
    case Header:
      config.log("using Http Header authentication");

      String oauthHeader = "Basic " + base64Encode(String.format("%s:%s", config.getApiKey(), config.getApiSecret()));
      request.addHeader(OAuthConstants.HEADER, oauthHeader);
      break;
    case QueryString:
      config.log("using Querystring authentication");

      request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
      request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
      break;
    case Body:
      config.log("using Body authentication");

      request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
      request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
     break;
    }
  }

  private String base64Encode(String format)
  {
    return new String(Base64.encodeBase64(format.getBytes()));
  }
}
