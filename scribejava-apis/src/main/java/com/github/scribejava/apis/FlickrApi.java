package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

/**
 * OAuth API for Flickr.
 *
 * @see <a href="http://www.flickr.com/services/api/">Flickr API</a>
 */
public class FlickrApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://www.flickr.com/services/oauth/authorize?oauth_token=%s";

    public enum FLICKR_PERM {
        READ, WRITE, DELETE
    };

    private String permString; /* read, write, or delete (delete includes read/write) */

    public FlickrApi() {
        permString = null;
    }

    public FlickrApi(FLICKR_PERM perm) {
        switch(perm) {
            case READ:
                this.permString = "read";
                break;
            case WRITE:
                this.permString = "write";
                break;
            case DELETE:
                this.permString = "delete";
        }
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
        String authUrl = String.format(AUTHORIZE_URL, requestToken.getToken());

        if (permString != null) {
            authUrl += "&perms=" + permString;
        }

        return authUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestTokenEndpoint() {
        return "https://www.flickr.com/services/oauth/request_token";
    }
}
