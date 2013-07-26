package org.scribe.oauth;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;

public class LinkedIn20ServiceImpl extends OAuth20ServiceImpl {
  public LinkedIn20ServiceImpl(final DefaultApi20 api, final OAuthConfig config) {
    super(api, config);
  }

  @Override
  public void signRequest(Token accessToken, OAuthRequest request) {
    request.addQuerystringParameter("oauth2_access_token", accessToken.getToken());
  }
}
