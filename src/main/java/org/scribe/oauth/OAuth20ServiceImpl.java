package org.scribe.oauth;

import java.util.concurrent.TimeUnit;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

public class OAuth20ServiceImpl implements OAuthService {
  private static final String VERSION = "2.0";

  private final DefaultApi20 api;
  private final OAuthConfig config;

  /**
   * Default constructor
   *
   * @param  api  OAuth2.0 api information
   * @param  config  OAuth 2.0 configuration param object
   */
  public OAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
    this.api = api;
    this.config = config;
  }

  /** {@inheritDoc} */
  public Token getAccessToken(Token requestToken, Verifier verifier) {
    Response response = createAccessTokenRequest(verifier).send();
    return api.getAccessTokenExtractor().extract(response.getBody());
  }

  protected OAuthRequest createAccessTokenRequest(final Verifier verifier) {
    OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
    request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
    request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
    request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
    request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
    if (config.hasScope()) {
      request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
    }
    if (config.getConnectTimeout() != null) {
      request.setConnectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS);
    }
    if (config.getReadTimeout() != null) {
      request.setReadTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS);
    }
    return request;
  }

  /** {@inheritDoc} */
  public Token getRequestToken() {
    throw new UnsupportedOperationException("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
  }

  /** {@inheritDoc} */
  public String getVersion() {
    return VERSION;
  }

  /** {@inheritDoc} */
  public void signRequest(Token accessToken, OAuthRequest request) {
    request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
  }

  /** {@inheritDoc} */
  public String getAuthorizationUrl(Token requestToken) {
    return api.getAuthorizationUrl(config);
  }

  protected OAuthConfig getConfig() {
    return config;
  }

  public DefaultApi20 getApi() {
    return api;
  }
}
