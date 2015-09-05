package org.scribe.services;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

public class DefaultRequestFactory implements RequestFactory
{
  public OAuthRequest createRequest(Verb verb, String url)
  {
    return new OAuthRequest(verb, url);
  }
}
