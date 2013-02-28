package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class GitHubApi extends DefaultApi20 {
    private static final String AUTHORIZE_URL = "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s";
	  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

	  @Override
	  public String getAccessTokenEndpoint()
	  {
	    return "https://github.com/login/oauth/access_token";
	  }

	  @Override
	  public String getAuthorizationUrl(OAuthConfig config)
	  {
	    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url which will get the code ");

	    // Append scope if present
	    if(config.hasScope())
	    {
	     return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
	    }
	    else
	    {
	      return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
	    }
	  }
	}

