package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class DoktornaraboteApi extends DefaultApi20 {

    protected DoktornaraboteApi() {
    }

    private static class InstanceHolder {
        private static final DoktornaraboteApi INSTANCE = new DoktornaraboteApi();
    }

    public static DoktornaraboteApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://auth.doktornarabote.ru/OAuth/Token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://auth.doktornarabote.ru/OAuth/Authorize";
    }
}
