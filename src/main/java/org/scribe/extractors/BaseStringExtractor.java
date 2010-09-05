package org.scribe.extractors;

import org.scribe.model.*;

public interface BaseStringExtractor
{
  String extract(OAuthRequest request);
}
