package com.github.scribejava.apis;

import com.github.scribejava.apis.openid.OpenIdJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;

public class TwitterApi20 extends DefaultApi20 {

  protected TwitterApi20() {
  }

  private static class InstanceHolder {

    private static final TwitterApi20 INSTANCE = new TwitterApi20();
  }

  public static TwitterApi20 instance() {
    return InstanceHolder.INSTANCE;
  }

  @Override
  public String getAccessTokenEndpoint() {
    return "https://api.twitter.com/2/oauth2/token";
  }

  @Override
  protected String getAuthorizationBaseUrl() {
    return "https://developer.twitter.com/2/oauth2/consent";
  }

  @Override
  public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
    return OpenIdJsonTokenExtractor.instance();
  }

  @Override
  public String getRevokeTokenEndpoint() {
    return "https://api.twitter.com/2/oauth2/revoke";
  }
}
