package org.scribe.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.apache.commons.codec.CharEncoding;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

public class OdnoklassnikiServiceImpl extends OAuth20ServiceImpl {
  private final DefaultApi20 api;
  private final OAuthConfig config;

  public OdnoklassnikiServiceImpl(final DefaultApi20 api, final OAuthConfig config) {
    super(api, config);
    this.api = api;
    this.config = config;
  }

  @Override
  public Token getAccessToken(final Token requestToken, final Verifier verifier) {
    final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
    request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
    request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
    request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
    request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
    request.addQuerystringParameter("grant_type", "authorization_code");
    if (config.hasScope()) {
      request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
    }
    final Response response = request.send();
    return api.getAccessTokenExtractor().extract(response.getBody());
  }

  @Override
  public void signRequest(final Token accessToken, final OAuthRequest request) {
    // sig = md5( request_params_composed_string+ md5(access_token + application_secret_key)  )
    try {
      final String tokenDigest = md5Hex((accessToken.getToken() + config.getApiSecret()));

      final String completeUrl = request.getCompleteUrl();
      final int queryIndex = completeUrl.indexOf('?');
      if (queryIndex != -1) {
        final String sigSource = URLDecoder.decode(completeUrl.substring(queryIndex + 1).replace("&", ""), CharEncoding.UTF_8) + tokenDigest;
        request.addQuerystringParameter("sig", md5Hex(sigSource));
      }

      super.signRequest(accessToken, request);
    } catch (UnsupportedEncodingException unex) {
      throw new IllegalStateException(unex);
    }
  }
}
