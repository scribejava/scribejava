package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;

/**
 * Box.com Api
 */
public class BoxApi20 extends DefaultApi20 {


    protected BoxApi20() {
    }

    private static class InstanceHolder {
        private static final BoxApi20 INSTANCE = new BoxApi20();
    }

    public static BoxApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.box.com/oauth2/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://account.box.com/api/oauth2/authorize";
    }

    @Override
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
    }
}
