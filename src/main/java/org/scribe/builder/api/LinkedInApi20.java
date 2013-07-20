package org.scribe.builder.api;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * OAuth 2.0 API for LinkedIn.
 *
 * @author Ludovic Claude
 */
public class LinkedInApi20 extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=%s&state=%s&redirect_uri=%s";
  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";
  private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";

  private final Set<String> scopes;

  public LinkedInApi20()
  {
    scopes = Collections.emptySet();
  }

  public LinkedInApi20(Set<String> scopes)
  {
    this.scopes = Collections.unmodifiableSet(scopes);
  }

  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }

  @Override
  public String getAccessTokenEndpoint()
  {
    return ACCESS_TOKEN_URL;
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. LinkedIn20 does not support OOB");
    // Append scope if present
    if (config.hasScope())
    {
      return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getState()), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
    }
    else
    {
      return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getState()), OAuthEncoder.encode(config.getCallback()));
    }
  }

  @Override
  public OAuthService createService(OAuthConfig config)
  {
    return new OAuth20ServiceImpl(this, config, new TimeoutTuner(2, TimeUnit.SECONDS), new LinkedInRequestTuner()) {
      public void signRequest(Token accessToken, OAuthRequest request)
      {
        request.addQuerystringParameter("oauth2_access_token", accessToken.getToken());
      }
    };
  }

  private String scopesAsString()
  {
    StringBuilder builder = new StringBuilder();
    for(String scope : scopes)
    {
      builder.append("+" + scope);
    }
    return builder.substring(1);
  }

  public static LinkedInApi withScopes(String... scopes)
  {
    Set<String> scopeSet = new HashSet<String>(Arrays.asList(scopes));
    return new LinkedInApi(scopeSet);
  }

  private static class LinkedInRequestTuner extends RequestTuner
  {
    @Override
    public void tune(Request request)
    {
      request.addHeader("Accept", "application/json, application/*+json");
      request.addHeader("x-li-format", "json");
      request.addHeader("Connection", "Keep-Alive");
      request.addBodyParameter("grant_type", "authorization_code");
    }
  }
}
