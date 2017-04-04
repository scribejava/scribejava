package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.OAuth2SignatureType;

/**
 * SinaWeibo OAuth 2.0 api.
 */
public class SinaWeiboApi20 extends DefaultApi20 {

    protected SinaWeiboApi20() {
    }

    private static class InstanceHolder {
        private static final SinaWeiboApi20 INSTANCE = new SinaWeiboApi20();
    }

    public static SinaWeiboApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.weibo.com/oauth2/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.weibo.com/oauth2/authorize";
    }

    @Override
    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_URI_QUERY_PARAMETER;
    }
}
