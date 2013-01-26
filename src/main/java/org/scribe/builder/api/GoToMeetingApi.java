package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;

/**
 * OAuth 2 Client for GoToMeeting, GoToTraining, and GoToWebinar REST APIs.
 * https://developer.citrixonline.com/page/authentication-and-authorization
 */
public class GoToMeetingApi extends DefaultApi20
{

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://api.citrixonline.com/oauth/access_token?grant_type=authorization_code";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig oAuthConfig)
  {
    final String url = String.format("https://api.citrixonline.com/oauth/authorize?client_id=%s", oAuthConfig.getApiKey());
    final String callback = oAuthConfig.getCallback();
    if (callback != null && callback.trim().length() != 0)
    {
      return String.format(url + "&redirect_uri=%s", OAuthEncoder.encode(callback));
    }
    else
    {
      return url;
    }
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }

  @Override
  public OAuthService createService(OAuthConfig config)
  {
    return new OAuth20ServiceImpl(this, config)
    {
      // GoToMeeting expects the OAuth 1 oauth_token header not the OAuth 2 access_token header.
      @Override
      public void signRequest(Token accessToken, OAuthRequest request)
      {
        request.addQuerystringParameter(OAuthConstants.TOKEN, accessToken.getToken());
      }
    };
  }
}
