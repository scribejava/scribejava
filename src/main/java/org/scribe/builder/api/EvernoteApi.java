package org.scribe.builder.api;

public class EvernoteApi extends DefaultApi10a
{

	@Override
	public String getRequestTokenEndpoint()
  {
		return "https://www.evernote.com/oauth";
	}

	@Override
	public String getAccessTokenEndpoint()
  {
		return "https://www.evernote.com/oauth";
	}

}
