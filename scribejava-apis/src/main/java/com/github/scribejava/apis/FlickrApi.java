package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;

/**
 * OAuth API for Flickr.
 *
 * @see <a href="http://www.flickr.com/services/api/">Flickr API</a>
 */
public class FlickrApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://www.flickr.com/services/oauth/authorize";

    public enum FlickrPerm {
        READ, WRITE, DELETE
    };

    /**
     * read, write, or delete (delete includes read/write)
     */
    private final String permString;

    protected FlickrApi() {
        permString = null;
    }

    protected FlickrApi(FlickrPerm perm) {
        permString = perm.name().toLowerCase();
    }

    private static class InstanceHolder {
        private static final FlickrApi INSTANCE = new FlickrApi();
    }

    public static FlickrApi instance() {
        return InstanceHolder.INSTANCE;
    }

    public static FlickrApi instance(FlickrPerm perm) {
        return perm == null ? instance() : new FlickrApi(perm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.flickr.com/services/oauth/access_token";
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return permString == null ? AUTHORIZE_URL : AUTHORIZE_URL + "?perms=" + permString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestTokenEndpoint() {
        return "https://www.flickr.com/services/oauth/request_token";
    }
}
