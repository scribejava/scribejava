package com.github.scribejava.apis.slack;

import com.github.scribejava.core.model.OAuth2AccessToken;

import java.util.Objects;

public class SlackOAuth2AccessToken extends OAuth2AccessToken {

    private final String userAccessToken;

    public SlackOAuth2AccessToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken, String scope, String userAccessToken, String rawResponse) {
        super(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
        this.userAccessToken = userAccessToken;
    }

    public String getUserAccessToken() {
        return userAccessToken;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + Objects.hashCode(userAccessToken);
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

        return Objects.equals(userAccessToken, ((SlackOAuth2AccessToken) obj).getUserAccessToken());
    }

}
