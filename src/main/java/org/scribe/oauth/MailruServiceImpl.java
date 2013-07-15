package org.scribe.oauth;

import java.util.concurrent.TimeUnit;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verifier;

public class MailruServiceImpl extends OAuth20ServiceImpl {
  public MailruServiceImpl(DefaultApi20 api, OAuthConfig config) {
    super(api, config);
  }

  @Override
  protected OAuthRequest createAccessTokenRequest(final Verifier verifier) {
    OAuthRequest request = new OAuthRequest(getApi().getAccessTokenVerb(), getApi().getAccessTokenEndpoint());
    request.addBodyParameter(OAuthConstants.CLIENT_ID, getConfig().getApiKey());
    request.addBodyParameter(OAuthConstants.CLIENT_SECRET, getConfig().getApiSecret());
    request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
    request.addBodyParameter(OAuthConstants.REDIRECT_URI, getConfig().getCallback());
    request.addBodyParameter(OAuthConstants.GRANT_TYPE, "authorization_code");

    if (getConfig().hasScope()) {
      request.addBodyParameter(OAuthConstants.SCOPE, getConfig().getScope());
    }
    if (getConfig().getConnectTimeout() != null) {
      request.setConnectTimeout(getConfig().getConnectTimeout(), TimeUnit.MILLISECONDS);
    }
    if (getConfig().getReadTimeout() != null) {
      request.setReadTimeout(getConfig().getReadTimeout(), TimeUnit.MILLISECONDS);
    }
    return request;
  }
}
