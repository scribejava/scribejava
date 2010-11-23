package org.scribe.builder.api;

import org.scribe.model.*;

public class GoogleApi extends DefaultApi10a
{
  @Override
  protected String getAccessTokenEndpoint()
  {
    return "https://www.google.com/accounts/OAuthGetAccessToken"; 
  }

  @Override
  protected String getRequestTokenEndpoint()
  {
    return "https://www.google.com/accounts/OAuthGetRequestToken";
  }

  @Override
  protected Verb getAccessTokenVerb()
  {
    return Verb.GET;
  }

  @Override
  protected Verb getRequestTokenVerb()
  {
    return Verb.GET;
  }
}
