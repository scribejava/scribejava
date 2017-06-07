package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.oauth.OAuthService;
import org.scribe.oauth.OAuthImgUr3Impl;

import org.scribe.model.Verb;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.extractors.AccessTokenExtractor;

/**
 * ImgUr API ver3 (OAuth 2)
 *
 * @author Brian Hsu
 *
 * @see <a href="http://api.imgur.com/">ImgUr API</a>
 */
public class ImgUr3Api extends DefaultApi20 {

  private static final String authorizeURL = "https://api.imgur.com/oauth2/authorize";

  private boolean isOOB(OAuthConfig config) {
    return "oob".equals(config.getCallback());
  }

  @Override 
  public Verb getAccessTokenVerb() { 
    return Verb.POST;
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor() { 
    return new JsonTokenExtractor();
  }

  @Override
  public String getAccessTokenEndpoint() { 
    return "https://api.imgur.com/oauth2/token";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config) {
    String apiKey = config.getApiKey();
    if (isOOB(config)) {
      return String.format("%s?client_id=%s&response_type=pin", authorizeURL, apiKey);
    } else {
      return String.format("%s?client_id=%s&response_type=code", authorizeURL, apiKey);
    }
  }

  @Override 
  public OAuthService createService(OAuthConfig config) {
    return new OAuthImgUr3Impl(this, config);
  }
}
