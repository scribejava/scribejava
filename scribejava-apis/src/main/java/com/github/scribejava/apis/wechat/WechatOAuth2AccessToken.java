package com.github.scribejava.apis.wechat;

import com.github.scribejava.core.model.OAuth2AccessToken;

public class WechatOAuth2AccessToken extends OAuth2AccessToken {

    private final String openid;

    public WechatOAuth2AccessToken(String accessToken, Integer expiresIn, String refreshToken, String scope,
                                   String openid, String response) {
        super(accessToken, null, expiresIn, refreshToken, scope, response);
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WechatOAuth2AccessToken)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final WechatOAuth2AccessToken that = (WechatOAuth2AccessToken) o;

        return openid.equals(that.openid);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + openid.hashCode();
        return result;
    }
}
