package org.scribe.builder.api;

import org.scribe.model.Verb;

public class EvernoteApi extends DefaultApi10a
{
  @Override
  public Verb getRequestTokenVerb()
  {
    return Verb.GET;
  }

	@Override
	public String getRequestTokenEndpoint()
  {
		return "https://www.evernote.com/oauth";
	}

	@Override
	public Verb getAccessTokenVerb()
	{
	  return Verb.GET;
	}

	@Override
	public String getAccessTokenEndpoint()
	{
		return "https://www.evernote.com/oauth";
	}
}
