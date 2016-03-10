package com.github.scribejava.apis;

import com.github.scribejava.apis.constantcontact.ConstantContactTokenExtractor;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.utils.OAuthEncoder;

public class ConstantContactApi2 extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "https://oauth2.constantcontact.com/oauth2/oauth/siteowner/authorize?client_id=%s&response_type=code"
            + "&redirect_uri=%s";

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
        return "https://oauth2.constantcontact.com/oauth2/oauth/token?grant_type=" + OAuthConstants.AUTHORIZATION_CODE;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return ConstantContactTokenExtractor.instance();
    }
}
