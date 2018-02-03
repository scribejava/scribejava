package com.github.scribejava.apis;

import com.github.scribejava.apis.service.WechatService;
import com.github.scribejava.apis.wechat.WechatOAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.builder.api.OAuth2SignatureType;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.util.Arrays;
import java.util.Map;

import static com.github.scribejava.apis.wechat.WechatOAuth2Constants.*;

public class WechatApi extends DefaultApi20 {

    protected WechatApi() {
    }

    private static class InstanceHolder {
        private static final WechatApi INSTANCE = new WechatApi();
    }

    public static WechatApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT_URL;
    }

    @Override
    public String getRefreshTokenEndpoint() {
        return REFRESH_TOKEN_ENDPOINT_URL;
    }

    @Override
    public WechatOAuth2AccessTokenJsonExtractor getAccessTokenExtractor() {
        return WechatOAuth2AccessTokenJsonExtractor.instance();
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    /**
     * Reference https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842
     * The authorization provided by WeChat does not fully reference the oauth2.0.
     * For example, the parameter name is not the same, and the parameter order requirements.
     * so I have to override this method to meet the requirements of it.
     */
    @Override
    public String getAuthorizationUrl(OAuthConfig config, Map<String, String> additionalParams) {
        final ParameterList parameters = new ParameterList(additionalParams);

        parameters.add(CLIENT_ID, config.getApiKey());

        final String callback = config.getCallback();
        if (callback == null) {
            throw new IllegalArgumentException("Missing required parameter 'redirect_uri'.");
        }
        parameters.add(REDIRECT_URI, callback);

        final String responseType = config.getResponseType();
        if (!CODE_RESPONSE_TYPE.equals(responseType)) {
            throw new IllegalArgumentException("Parameter response_type must be 'code'.");
        }
        parameters.add(RESPONSE_TYPE, config.getResponseType());

        final String scope = config.getScope();
        if (scope == null || Arrays.binarySearch(SCOPE_VALUES, scope) < 0) {
            throw new IllegalArgumentException("Parameter scope can only be 'snsapi_base' or 'snsapi_userinfo'.");
        }
        parameters.add(SCOPE, scope);

        final String state = config.getState();
        if (state != null) {
            parameters.add(STATE, state);
        }

        return parameters.appendTo(getAuthorizationBaseUrl()).concat("#wechat_redirect");
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new WechatService(this, config);
    }

    @Override
    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_URI_QUERY_PARAMETER;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }
}
