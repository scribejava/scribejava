package org.scribe.builder.api;

import org.scribe.extensions.linkedin.*;
import org.scribe.extractors.*;
import org.scribe.model.Token;

public class LinkedInApi extends DefaultApi10a
{
  private static final String AUTHORIZE_URL = "https://api.linkedin.com/uas/oauth/authorize?oauth_token=%s";

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://api.linkedin.com/uas/oauth/accessToken";
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "https://api.linkedin.com/uas/oauth/requestToken";
  }
  
  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }
  
  @Override
  public BaseStringExtractor getBaseStringExtractor()
  {
    return new LinkedInBaseStringExtractorImpl();
  }

}
