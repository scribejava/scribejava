package com.github.scribejava.apis.service;

import com.github.scribejava.apis.wechat.WeChatConstants;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;

public class WeChatOAuth20ServiceImpl extends OAuth20Service {

    public WeChatOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    @Override
    protected OAuthRequest createAccessTokenRequest(String code) {

        final DefaultApi20 api = getApi();
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        final OAuthConfig config = getConfig();

        request.addParameter(WeChatConstants.CLIENT_ID, config.getApiKey());
        request.addParameter(WeChatConstants.CLIENT_SECRET, config.getApiSecret());
        request.addParameter(WeChatConstants.CODE, code);
        request.addParameter(WeChatConstants.GRANT_TYPE, WeChatConstants.AUTHORIZATION_CODE);

        return request;
    }

}
