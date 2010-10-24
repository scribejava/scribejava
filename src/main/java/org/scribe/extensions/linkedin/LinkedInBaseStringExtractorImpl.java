package org.scribe.extensions.linkedin;

import org.scribe.extractors.*;
import org.scribe.model.*;

/**
 * LinkedIn's specific extractor that does not encode the tilde character.
 * 
 * @author Pablo Fernandez
 */
public class LinkedInBaseStringExtractorImpl extends BaseStringExtractorImpl
{

  /**
   * {@inheritDoc}
   */
  @Override
  public String extract(OAuthRequest request)
  {
    String baseString = super.extract(request);
    return baseString.replace("%7E", "~");
  }
}
