package org.scribe.builder.api;

import org.scribe.extensions.linkedin.*;
import org.scribe.extractors.*;

public class LinkedInApi extends DefaultApi10a
{

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
  public BaseStringExtractor getBaseStringExtractor()
  {
    return new LinkedInBaseStringExtractorImpl();
  }

}
