package org.scribe.extensions.linkedin;

import org.scribe.extractors.*;
import org.scribe.model.*;

public class LinkedInBaseStringExtractorImpl extends BaseStringExtractorImpl
{

  @Override
  public String extract(OAuthRequest request)
  {
    String baseString = super.extract(request);
    return baseString.replace("%7E", "~");
  }
}
