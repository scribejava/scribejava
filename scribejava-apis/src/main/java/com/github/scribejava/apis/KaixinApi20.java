package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;

/**
 * Kaixin(http://www.kaixin001.com/) open platform api based on OAuth 2.0.
 */
public class KaixinApi20 extends DefaultApi20 {

    protected KaixinApi20() {
    }

    private static class InstanceHolder {
        private static final KaixinApi20 INSTANCE = new KaixinApi20();
    }

    public static KaixinApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.kaixin001.com/oauth2/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "http://api.kaixin001.com/oauth2/authorize";
    }

    @Override
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
    }
}
