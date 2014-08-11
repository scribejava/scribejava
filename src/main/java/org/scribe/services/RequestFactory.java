package org.scribe.services;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

public interface RequestFactory
{
    OAuthRequest createRequest(Verb verb, String url);
}
