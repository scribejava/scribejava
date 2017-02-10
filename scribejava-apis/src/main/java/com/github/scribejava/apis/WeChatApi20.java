package com.github.scribejava.apis;

import com.github.scribejava.apis.service.WeChatOAuth20ServiceImpl;
import com.github.scribejava.apis.wechat.WeChatConstants;
import com.github.scribejava.apis.wechat.WeChatJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.SignatureType;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.util.Map;

/**
 * WeChat OAuth 2.0 api.
 */
public class WeChatApi20 extends DefaultApi20 {

    protected WeChatApi20() {
    }

    private static class InstanceHolder {
        private static final WeChatApi20 INSTANCE = new WeChatApi20();
    }

    public static WeChatApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.weixin.qq.com/sns/oauth2/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://open.weixin.qq.com/connect/qrconnect";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config, Map<String, String> additionalParams) {

        final ParameterList parameters = new ParameterList(additionalParams);
        parameters.add(WeChatConstants.RESPONSE_TYPE, config.getResponseType());
        parameters.add(WeChatConstants.CLIENT_ID, config.getApiKey());

        final String callback = config.getCallback();
        if (callback != null) {
            parameters.add(WeChatConstants.REDIRECT_URI, callback);
        }

        final String scope = config.getScope();
        if (scope != null) {
            parameters.add(WeChatConstants.SCOPE, scope);
        }

        final String state = config.getState();
        if (state != null) {
            parameters.add(WeChatConstants.STATE, state);
        }

        return parameters.appendTo(getAuthorizationBaseUrl());
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new WeChatOAuth20ServiceImpl(this, config);
    }

    @Override
    public SignatureType getSignatureType() {
        return SignatureType.BEARER_URI_QUERY_PARAMETER;
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return WeChatJsonTokenExtractor.instance();
    }
}
