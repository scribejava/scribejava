package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.extractors.TokenExtractor20Impl;
import org.scribe.model.*;

import org.scribe.utils.*;
import static org.scribe.utils.URLUtils.*;

/**
 * Created by IntelliJ IDEA.
 * User: MohamadT
 * Date: 9/13/11
 * Time: 11:58 PM
 *
 */
public class Google2Api extends DefaultApi20
{
  private static final String AUTHORIZATION_URL = "https://accounts.google.com/o/oauth2/auth?client_id=%s&redirect_uri=%s";
	private static final String SCOPED_AUTHORIZATION_URL = AUTHORIZATION_URL + "&scope=%s&response_type=code";

	@Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }

	@Override
	public Verb getAccessTokenVerb()
	{
	  return Verb.POST;
	}

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://accounts.google.com/o/oauth2/token";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Google does not support OOB");

    // Append scope if present
    if(config.hasScope())
    {
     return String.format(SCOPED_AUTHORIZATION_URL, config.getApiKey(), formURLEncode(config.getCallback()), formURLEncode(config.getScope()));
    }
    else
    {
      return String.format(AUTHORIZATION_URL, config.getApiKey(), formURLEncode(config.getCallback()));
    }
  }
}
