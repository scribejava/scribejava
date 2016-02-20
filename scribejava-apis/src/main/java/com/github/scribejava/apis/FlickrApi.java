package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

/**
 * OAuth API for Flickr.
 *
 * @see <a href="http://www.flickr.com/services/api/">Flickr API</a>
 */
public class FlickrApi extends DefaultApi10a {

    protected FlickrApi() {
    }

    private static class InstanceHolder {
        private static final FlickrApi INSTANCE = new FlickrApi();
    }

    public static FlickrApi instance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.flickr.com/services/oauth/access_token";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return "https://www.flickr.com/services/oauth/authorize?oauth_token=" + requestToken.getToken();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestTokenEndpoint() {
        return "https://www.flickr.com/services/oauth/request_token";
    }
}
