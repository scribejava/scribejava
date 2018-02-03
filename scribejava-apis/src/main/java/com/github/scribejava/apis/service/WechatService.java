package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.util.Map;

import static com.github.scribejava.apis.wechat.WechatOAuth2Constants.*;


public class WechatService extends OAuth20Service {

    public WechatService(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    /**
     * The reason to override this method is the same as
     * {@link com.github.scribejava.apis.WechatApi#getAuthorizationUrl(OAuthConfig, Map)}
     */
    @Override
    protected OAuthRequest createAccessTokenRequest(String code) {
        final OAuthRequest request = new OAuthRequest(getApi().getAccessTokenVerb(), getApi().getAccessTokenEndpoint());
        final OAuthConfig config = getConfig();

        request.addParameter(CLIENT_ID, config.getApiKey());
        request.addParameter(CLIENT_SECRET, config.getApiSecret());

        request.addParameter(CODE, code);
        request.addParameter(GRANT_TYPE, AUTHORIZATION_CODE);

        return request;
    }

    /**
     * The reason to override this method is the same as
     * {@link com.github.scribejava.apis.WechatApi#getAuthorizationUrl(OAuthConfig, Map)}
     */
    @Override
    protected OAuthRequest createRefreshTokenRequest(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("The refreshToken cannot be null or empty");
        }
        final OAuthRequest request = new OAuthRequest(getApi().getAccessTokenVerb(), getApi().getRefreshTokenEndpoint());

        request.addParameter(CLIENT_ID, getConfig().getApiKey());

        request.addParameter(OAuthConstants.GRANT_TYPE, REFRESH_TOKEN);
        request.addParameter(REFRESH_TOKEN, refreshToken);

        return request;
    }

    @Override
    protected OAuthRequest createAccessTokenPasswordGrantRequest(String username, String password) {
        throw new UnsupportedOperationException("WeChat doesn't support Resource Owner Password Credentials Grant");
    }

    @Override
    protected OAuthRequest createAccessTokenClientCredentialsGrantRequest() {
        throw new UnsupportedOperationException("WeChat doesn't support Client Credentials Grant");
    }
}
