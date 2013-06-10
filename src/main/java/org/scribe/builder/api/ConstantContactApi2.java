package org.scribe.builder.api;

import java.util.regex.*;
import org.scribe.exceptions.*;
import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class ConstantContactApi2 extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://oauth2.constantcontact.com/oauth2/oauth/siteowner/authorize?client_id=%s&response_type=code&redirect_uri=%s";

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://oauth2.constantcontact.com/oauth2/oauth/token?grant_type=authorization_code";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
  }

  @Override
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new AccessTokenExtractor()
    {

      public Token extract(String response)
      {
        Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");

        String regex = "\"access_token\"\\s*:\\s*\"([^&\"]+)\"";
        Matcher matcher = Pattern.compile(regex).matcher(response);
        if (matcher.find())
        {
          String token = OAuthEncoder.decode(matcher.group(1));
          return new Token(token, "", response);
        } 
        else
        {
          throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
        }
      }
    };
  }
}