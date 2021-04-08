package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.services.HMACSha256SignatureService;
import com.github.scribejava.core.services.SignatureService;

public class NetSuiteApi extends DefaultApi10a {

    protected NetSuiteApi() {
    }

    private static class InstanceHolder {
        private static final NetSuiteApi INSTANCE = new NetSuiteApi();
    }

    public static NetSuiteApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public SignatureService getSignatureService() {
        return new HMACSha256SignatureService();
    }

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationBaseUrl() {
        return null;
    }

}
