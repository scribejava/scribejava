package org.scribe.builder.api;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;
import org.scribe.processors.extractors.*;
import org.scribe.model.*;

/**
 * @author Boris G. Tsirkin <mail@dotbg.name>
 * @since 20.4.2011
 */
public class VkontakteApi extends DefaultApi20 {
  private static final String AUTHORIZE_URL = "https://oauth.vk.com/authorize";

  @Override
  public String getAccessTokenEndpoint() {
    return "https://api.vkontakte.ru/oauth/access_token";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config, String state) {
      AuthUrlBuilder builder = new DefaultAuthUrlBuilder();

      builder.setEndpoint(AUTHORIZE_URL)
              .setClientId(config.getApiKey())
              .setRedirectUrl(config.getCallback())
              .setScope(config.getScope())
              .setState(state)
              .setResponseType(OAuthConstants.CODE);
      return builder.build();
  }

  @Override
  public TokenExtractor getAccessTokenExtractor() {
    return new JsonTokenExtractor();
  }
}
