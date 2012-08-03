package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;

/**
 * OAuth 2.0 API for Google.
 * 
 * @see <a href="https://developers.google.com/accounts/docs/OAuth2">OAuth 2.0 to Access Google APIs</a>
 */
public class GoogleApi20 extends DefaultApi20 {

  private static final String AUTHORIZE_URL = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=%s&redirect_uri=%s";
  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

  @Override
  public String getAccessTokenEndpoint() {
    return "https://accounts.google.com/o/oauth2/token";
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor() {
    // possible regex: "\"access_token\" : \"([^&\"]+)\""
    return new JsonTokenExtractor();
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config) {
    String authorizationUrl;
    // Append scope if present
    if (config.hasScope()) {
      authorizationUrl = String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()),
          OAuthEncoder.encode(config.getScope()));
    } else {
      authorizationUrl = String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }
    // Access type added to manage offline mode with refresh token
    if (config.hasAccessType()) {
      authorizationUrl += "&access_type=" + config.getAccessType();
    }
    return authorizationUrl;
  }

  @Override
  public Verb getAccessTokenVerb() {
    return Verb.POST;
  }

}
