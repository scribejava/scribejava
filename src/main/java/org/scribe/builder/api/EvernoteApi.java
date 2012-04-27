package org.scribe.builder.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class EvernoteApi extends DefaultApi10a
{
  private static final String EVERNOTE_URL = "https://www.evernote.com";
  
  @Override
  public Verb getRequestTokenVerb()
  {
    return Verb.GET;
  }

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
	
	public static class EvernoteAuthToken extends Token {
    private static final long serialVersionUID = 5913745981744917828L;

    private String noteStoreUrl;
	  private String webApiUrlPrefix;
	  private int userId;

	  public EvernoteAuthToken(String token, String secret,
	      String noteStoreUrl, String webApiUrlPrefix, int userId, String rawResponse) {
	    super(token, secret, rawResponse);
	    this.noteStoreUrl = noteStoreUrl;
	    this.webApiUrlPrefix = webApiUrlPrefix;
	    this.userId = userId;
	  }
	  
	  /**
	   * Get the Evernote web service NoteStore URL from the OAuth access token response.
	   */
	  public String getNoteStoreUrl() {
	    return noteStoreUrl;
	  }

	  /**
	   * Get the Evernote web API URL prefix from the OAuth access token response.
	   */
	  public String getWebApiUrlPrefix() {
	    return webApiUrlPrefix;
	  }

	  /**
	   * Get the numeric Evernote user ID from the OAuth access token response.
	   */
	  public int getUserId() {
	    return userId;
	  }
	}
	
	public static class EvernoteAccessTokenExtractor implements org.scribe.extractors.AccessTokenExtractor {
	  
	  private static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");
	  private static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]*)");
	  private static final Pattern NOTESTORE_REGEX = Pattern.compile("edam_noteStoreUrl=([^&]+)");
	  private static final Pattern WEBAPI_REGEX = Pattern.compile("edam_webApiUrlPrefix=([^&]+)");
	  private static final Pattern USERID_REGEX = Pattern.compile("edam_userId=([^&]+)");
	  
	  /**
	   * {@inheritDoc} 
	   */
	  public Token extract(String response)
	  {
	    Preconditions.checkEmptyString(response, "Response body is incorrect. " +
	        "Can't extract a token from an empty string");
	    return new EvernoteAuthToken(extract(response, TOKEN_REGEX), 
	        extract(response, SECRET_REGEX), 
	        extract(response, NOTESTORE_REGEX), 
	        extract(response, WEBAPI_REGEX), 
	        Integer.parseInt(extract(response, USERID_REGEX)), 
	        response);
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
