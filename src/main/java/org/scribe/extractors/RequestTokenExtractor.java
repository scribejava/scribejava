package org.scribe.extractors;

import org.scribe.model.*;

public interface RequestTokenExtractor
{

  public Token extract(String response);
}
