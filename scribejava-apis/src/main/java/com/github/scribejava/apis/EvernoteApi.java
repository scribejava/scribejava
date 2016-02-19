package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class EvernoteApi extends DefaultApi10a {

    protected EvernoteApi() {
    }

    private static class InstanceHolder {
        private static final EvernoteApi INSTANCE = new EvernoteApi();
    }

    public static EvernoteApi instance() {
        return InstanceHolder.INSTANCE;
    }

    protected String serviceUrl() {
        return "https://www.evernote.com";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return serviceUrl() + "/oauth";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return serviceUrl() + "/oauth";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(serviceUrl() + "/OAuth.action?oauth_token=%s", requestToken.getToken());
    }

    /**
     * Sandbox endpoint
     */
    public static class Sandbox extends EvernoteApi {

        private Sandbox() {
        }

        private static class InstanceHolder {
            private static final Sandbox INSTANCE = new Sandbox();
        }

        public static Sandbox instance() {
            return InstanceHolder.INSTANCE;
        }

        @Override
        protected String serviceUrl() {
            return "https://sandbox.evernote.com";
        }
    }

    /**
     * Yinxiang Biji endpoint
     */
    public static class Yinxiang extends EvernoteApi {

        private Yinxiang() {
        }

        private static class InstanceHolder {
            private static final Yinxiang INSTANCE = new Yinxiang();
        }

        public static Yinxiang instance() {
            return InstanceHolder.INSTANCE;
        }

        @Override
        protected String serviceUrl() {
            return "https://app.yinxiang.com";
        }
    }
}
