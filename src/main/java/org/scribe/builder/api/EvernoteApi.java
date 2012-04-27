package org.scribe.builder.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.Token;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class EvernoteApi extends DefaultApi10a
{
  private static final String EVERNOTE_URL = "https://www.evernote.com";
  
	@Override
	public String getRequestTokenEndpoint()
  {
		return EVERNOTE_URL + "/oauth";
	}

	@Override
	public String getAccessTokenEndpoint()
	{
		return EVERNOTE_URL + "/oauth";
	}
	
	@Override
	public String getAuthorizationUrl(Token requestToken)
	{
	  return String.format(EVERNOTE_URL + "/OAuth.action?oauth_token=%s", requestToken.getToken());
	}
	
	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
	  return new EvernoteAccessTokenExtractor();
	}

	public static class Sandbox extends EvernoteApi
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

	public static class EvernoteAccessTokenExtractor implements AccessTokenExtractor {
	  
	  private static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");
	  // Evernote access tokens include an empty token secret (the empty string).
	  private static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]*)");
	  
	  /**
	   * {@inheritDoc} 
	   */
	  public Token extract(String response)
	  {
	    Preconditions.checkEmptyString(response, "Response body is incorrect. " +
	        "Can't extract a token from an empty string");
	    return new Token(extract(response, TOKEN_REGEX), extract(response, SECRET_REGEX), response);
	  }
	  
	  private String extract(String response, Pattern p)
	  {
	    Matcher matcher = p.matcher(response);
	    if (matcher.find() && matcher.groupCount() >= 1)
	    {
	      return OAuthEncoder.decode(matcher.group(1));
	    }
	    else
	    {
	      throw new OAuthException("Response body is incorrect. " +
	          "Can't extract token and secret from this: '" + response + "'", null);
	    }
	  }
	}
	
}
