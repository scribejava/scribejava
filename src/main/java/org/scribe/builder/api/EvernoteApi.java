package org.scribe.builder.api;

import org.scribe.model.Verb;

public class EvernoteApi extends DefaultApi10a
{
  @Override
  protected Verb getRequestTokenVerb()
  {
    return Verb.GET;
  }

	@Override
	public String getRequestTokenEndpoint()
  {
		return "https://www.evernote.com/oauth";
	}

	@Override
	protected Verb getAccessTokenVerb()
	{
	  return Verb.GET;
	}

	@Override
	public String getAccessTokenEndpoint()
	{
		return "https://www.evernote.com/oauth";
	}
}
