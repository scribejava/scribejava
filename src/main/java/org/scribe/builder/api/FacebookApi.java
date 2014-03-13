package org.scribe.builder.api;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;
import org.scribe.model.*;

public class FacebookApi extends DefaultApi20 {
  private static final String AUTHORIZE_URL = "https://www.facebook.com/dialog/oauth";

  @Override
  public String getAccessTokenEndpoint() {
    return "https://graph.facebook.com/oauth/access_token";
  }

  @Override
  public String getAuthorizationUrl(final OAuthConfig config, final String state) {
      AuthUrlBuilder builder = new DefaultAuthUrlBuilder();

      builder.setEndpoint(AUTHORIZE_URL)
              .setClientId(config.getApiKey())
              .setRedirectUrl(config.getCallback())
              .setScope(config.getScope())
              .setState(state);

    return builder.build();
  }
}
