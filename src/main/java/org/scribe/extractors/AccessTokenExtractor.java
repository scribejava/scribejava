package org.scribe.extractors;

import org.scribe.model.*;

public interface AccessTokenExtractor
{
  public Token extract(String response);
}
