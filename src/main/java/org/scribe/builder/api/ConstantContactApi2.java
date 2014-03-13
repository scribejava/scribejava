package org.scribe.builder.api;

import java.util.regex.*;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;
import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.processors.extractors.TokenExtractor;
import org.scribe.utils.*;

public class ConstantContactApi2 extends DefaultApi20 {
  private static final String AUTHORIZE_URL = "https://oauth2.constantcontact.com/oauth2/oauth/siteowner/authorize";

  @Override
  public String getAccessTokenEndpoint() {
    return "https://oauth2.constantcontact.com/oauth2/oauth/token?grant_type=authorization_code";
  }

  @Override
  public String getAuthorizationUrl(final OAuthConfig config, final String state) {
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
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }

  @Override
  public TokenExtractor getAccessTokenExtractor() {
    return new TokenExtractor() {

      public Token extract(final String response) {
        Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");

        String regex = "\"access_token\"\\s*:\\s*\"([^&\"]+)\"";
        Matcher matcher = Pattern.compile(regex).matcher(response);
        if (matcher.find()) {
          String token = OAuthEncoder.decode(matcher.group(1));
          return new Token(token, "", response);
        } else {
          throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
        }
      }
    };
  }
}