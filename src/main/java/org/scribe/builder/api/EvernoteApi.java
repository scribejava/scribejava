package org.scribe.builder.api;

import org.scribe.model.Token;
import org.scribe.model.Verb;

public class EvernoteApi extends DefaultApi10a
{
  private static final String AUTHORIZATION_URL = "https://www.evernote.com/OAuth.action?oauth_token=%s";
  
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
	
	@Override
	public String getAuthorizationUrl(Token requestToken)
	{
	  return String.format(AUTHORIZATION_URL, requestToken.getToken());
	}

	public static class Sandbox extends EvernoteApi
	{
	  private static final String SANDBOX_URL = "https://sandbox.evernote.com/oauth";

	  @Override
	  public String getRequestTokenEndpoint()
	  {
	    return SANDBOX_URL;
	  }

	  @Override
	  public String getAccessTokenEndpoint()
	  {
	    return SANDBOX_URL;
	  }

	  @Override
	  public String getAuthorizationUrl(Token requestToken)
	  {
	    return String.format(SANDBOX_URL + "?oauth_token=%s", requestToken.getToken());
	  }
	}
}
