package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;

public class Foursquare2Api extends DefaultApi20 {

    protected Foursquare2Api() {
    }

    private static class InstanceHolder {
        private static final Foursquare2Api INSTANCE = new Foursquare2Api();
    }

    public static Foursquare2Api instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://foursquare.com/oauth2/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://foursquare.com/oauth2/authenticate";
    }

    @Override
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
    }
}
