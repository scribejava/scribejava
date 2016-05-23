package com.github.scribejava.apis;

import com.github.scribejava.apis.constantcontact.ConstantContactTokenExtractor;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;

public class ConstantContactApi2 extends DefaultApi20 {

    protected ConstantContactApi2() {
    }

    private static class InstanceHolder {
        private static final ConstantContactApi2 INSTANCE = new ConstantContactApi2();
    }

    public static ConstantContactApi2 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://oauth2.constantcontact.com/oauth2/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://oauth2.constantcontact.com/oauth2/oauth/siteowner/authorize";
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return ConstantContactTokenExtractor.instance();
    }
}
