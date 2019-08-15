package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

/**
 * Dropbox.com Api
 */
public class DropboxApi extends DefaultApi20 {

    protected DropboxApi() {
    }

    private static class InstanceHolder {

        private static final DropboxApi INSTANCE = new DropboxApi();
    }

    public static DropboxApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.dropbox.com/oauth2/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://www.dropbox.com/oauth2/authorize";
    }
}
