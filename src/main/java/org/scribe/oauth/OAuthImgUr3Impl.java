package org.scribe.oauth;

import org.scribe.builder.api.DefaultApi20;

import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;

public class OAuthImgUr3Impl extends OAuth20ServiceImpl {

  private DefaultApi20 api;
  private OAuthConfig config;

  public OAuthImgUr3Impl(DefaultApi20 api, OAuthConfig config) {
    super(api, config);
    this.api = api;
    this.config = config;
  }

  private boolean isOOB(OAuthConfig config) {
    return "oob".equals(config.getCallback());
  }

  @Override
  public Token getAccessToken(Token requestToken, Verifier verifier) {

    OAuthRequest request = new OAuthRequest(
      api.getAccessTokenVerb(), 
      api.getAccessTokenEndpoint()
    );

    String grantType = isOOB(config) ? "pin" : "authorization_code";

    // ImgUr API ver 3 using POST to get token, and we need pass
    // parameters using Body parameter, query string will not work.
    request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
    request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
    request.addBodyParameter("grant_type", grantType);
    request.addBodyParameter("pin", verifier.getValue());

    Response response = request.send();

    return api.getAccessTokenExtractor().extract(response.getBody());
  }

  @Override
  public void signRequest(Token accessToken, OAuthRequest request) {

    if (accessToken != null) {
      request.addHeader("Authorization", "Bearer " + accessToken.getToken());
    } else {
      request.addHeader("Authorization", "Client-ID " + config.getApiKey());
    }
  }

}
