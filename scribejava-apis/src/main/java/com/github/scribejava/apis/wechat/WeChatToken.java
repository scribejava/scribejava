package com.github.scribejava.apis.wechat;

import com.github.scribejava.core.model.OAuth2AccessToken;

import java.util.Objects;

public class WeChatToken extends OAuth2AccessToken {

    // Unique identifier for Authorized user
    private final String openId;

    // User's unified identifier for a WeChat open platform account
    private final String unionId;

    public WeChatToken(String accessToken, String openId, String unionId, String rawResponse) {
        this(accessToken, null, null, null, null, openId, unionId, rawResponse);
    }

    public WeChatToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken, String scope,
                       String openId, String unionId, String rawResponse) {
        super(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
        this.openId = openId;
        this.unionId = unionId;

    }

    public String getOpenId() {
        return openId;
    }

    public String getUnionId() {
        return unionId;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + Objects.hashCode(openId);
        hash = 37 * hash + Objects.hashCode(unionId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!Objects.equals(openId, ((WeChatToken) obj).getOpenId())) {
            return false;
        }
        return Objects.equals(unionId, ((WeChatToken) obj).getUnionId());
    }

    @Override
    public String toString() {
        return "WeChatToken{"
                + "access_token=" + getAccessToken()
                + ", expires_in=" + getExpiresIn()
                + ", refresh_token=" + getRefreshToken()
                + ", openid=" + openId
                + ", scope=" + getScope()
                + ", unionid=" + unionId
                + '}';
    }
}
