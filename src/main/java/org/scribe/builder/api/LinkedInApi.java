package org.scribe.builder.api;

import org.scribe.extensions.linkedin.*;
import org.scribe.extractors.*;

public class LinkedInApi extends DefaultApi10a
{

  @Override
  protected String getAccessTokenEndpoint()
  {
    return "https://api.linkedin.com/uas/oauth/accessToken";
  }

  @Override
  protected String getRequestTokenEndpoint()
  {
    return "https://api.linkedin.com/uas/oauth/requestToken";
  }
  
  @Override
  protected BaseStringExtractor getBaseStringExtractor()
  {
    return new LinkedInBaseStringExtractorImpl();
  }

}
