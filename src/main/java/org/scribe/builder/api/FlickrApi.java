package org.scribe.builder.api;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;
import org.scribe.utils.OAuthEncoder;

/**
 * OAuth API for Flickr.
 * @author Darren Greaves
 * @see <a href="http://www.flickr.com/services/api/">Flickr API</a>
 */
public class FlickrApi extends DefaultApi10a
{

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAccessTokenEndpoint()
    {

        return "http://www.flickr.com/services/oauth/access_token";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorizationUrl(Token requestToken)
    {

        return "http://www.flickr.com/services/oauth/authorize?oauth_token=" + requestToken.getToken();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestTokenEndpoint()
    {

        return "http://www.flickr.com/services/oauth/request_token";
    }


    /**
     * Get request token endpoint with a callback URL.
     * 
     * @param callbackUrl
     * @return
     */
    public String getRequestTokenEndpoint(String callbackUrl)
    {

        return String.format("%s?oauth_callback=%s", getRequestTokenEndpoint(), OAuthEncoder.encode(callbackUrl));
    }
}
