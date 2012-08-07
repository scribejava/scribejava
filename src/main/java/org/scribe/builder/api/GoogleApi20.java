package org.scribe.builder.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

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
    return new AccessTokenExtractor() {

      private Pattern accessTokenPattern = Pattern.compile("\"access_token\"\\s*:\\s*\"([^&\"]+)\"");

      public Token extract(String response) {
        Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");

        Matcher matcher = accessTokenPattern.matcher(response);
        if (matcher.find()) {
          String token = OAuthEncoder.decode(matcher.group(1));
          return new Token(token, "", response);
        } else {
          throw new OAuthException("Cannot extract an acces token. Response was: " + response);
        }
      }
    };
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
      authorizationUrl += "&" + OAuthConstants.ACCESS_TYPE + "=" + config.getAccessType();
    }
    return authorizationUrl;
  }

  @Override
  public Verb getAccessTokenVerb() {
    return Verb.POST;
  }

  @Override
  public String getRefreshTokenParameterName() {
    return OAuthConstants.REFRESH_TOKEN;
  }
}
