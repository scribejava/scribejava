package org.scribe.builder.api;

import org.scribe.model.Token;

public class YinXiangApi extends DefaultApi10a
{
  private static final String AUTHORIZATION_URL = "https://app.yinxiang.com/OAuth.action?oauth_token=%s";

  @Override
	public String getRequestTokenEndpoint()
  {
    return "https://app.yinxiang.com/oauth";
  }

	@Override
	public String getAccessTokenEndpoint()
	{
	  return "https://app.yinxiang.com/oauth";
	}
	
	@Override
	public String getAuthorizationUrl(Token requestToken)
	{
	  return String.format(AUTHORIZATION_URL, requestToken.getToken());
	}

	public static class Sandbox extends YinXiangApi
	{
	  private static final String SANDBOX_URL = "https://sandbox.evernote.com";

	  @Override
	  public String getRequestTokenEndpoint()
	  {
	    return SANDBOX_URL + "/oauth";
	  }

	  @Override
	  public String getAccessTokenEndpoint()
	  {
      return SANDBOX_URL + "/oauth";
	  }

	  @Override
	  public String getAuthorizationUrl(Token requestToken)
	  {
	    return String.format(SANDBOX_URL + "/OAuth.action?oauth_token=%s", requestToken.getToken());
	  }
	}	
}
