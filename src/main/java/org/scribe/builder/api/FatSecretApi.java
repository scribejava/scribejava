package org.scribe.builder.api;

import org.scribe.model.*;

/**
 *
 * OAuth API for Fat Secret
 *
 * @author John Satriano
 * @see <a href="http://platform.fatsecret.com/api/>Fat Secret API</a>
 *
 */
public class FatSecret extends DefaultApi10a {

	  private static final String AUTHORIZE_URL = "http://fatsecret.com/oauth/authorize?token=%s";

    /**
     * {@inheritDoc}
     */
	  @Override
	  public String getAccessTokenEndpoint()
	  {
	    return "http://fatsecret.com/oauth/access_token";
	  }

    /**
     * {@inheritDoc}
     */
	  @Override
	  public String getRequestTokenEndpoint()
	  {
	    return "http://fatsecret.com/oauth/request_token";
	  }
	  
    /**
     * {@inheritDoc}
     */
	  @Override
	  public String getAuthorizationUrl(Token requestToken)
	  {
	    return String.format(AUTHORIZE_URL, requestToken.getToken());
	  }
	  
	}
