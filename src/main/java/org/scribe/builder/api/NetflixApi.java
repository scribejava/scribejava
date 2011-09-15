package org.scribe.builder.api;

import java.util.HashMap;
import java.util.Map;

import org.scribe.model.*;
import org.scribe.utils.MapUtils;

public class NetflixApi extends DefaultApi10a {
  private static final String AUTHORIZE_URL = "https://api-user.netflix.com/oauth/login?oauth_token=%s&%s";

  @Override
  public String getAccessTokenEndpoint() {
    return "http://api.netflix.com/oauth/access_token";
  }

  @Override
  public String getRequestTokenEndpoint() {
    return "http://api.netflix.com/oauth/request_token";
  }

  @Override
  public String getAuthorizationUrl(Token requestToken) {
    return getAuthorizationUrl(requestToken, new HashMap<String, String>());
  }

  @Override
  public String getAuthorizationUrl(Token requestToken, Map<String, String> requestKeyWords) {
    return String.format(AUTHORIZE_URL, requestToken.getToken(), MapUtils.mapToString(requestKeyWords));
  }
}
