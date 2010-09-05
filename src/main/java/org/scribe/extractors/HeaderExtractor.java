package org.scribe.extractors;

import org.scribe.model.*;

public interface HeaderExtractor
{
  String extract(OAuthRequest request);
}
