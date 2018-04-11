package com.github.scribejava.apis.fitbit;

import com.github.scribejava.core.model.OAuth2AccessToken;
import java.util.Objects;

public class FitBitOAuth2AccessToken extends OAuth2AccessToken {

    private static final long serialVersionUID = -6374486860742407411L;

    private final String userId;

    public FitBitOAuth2AccessToken(String accessToken, String openIdToken, String rawResponse) {
        this(accessToken, null, null, null, null, openIdToken, rawResponse);
    }

    public FitBitOAuth2AccessToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken,
            String scope, String userId, String rawResponse) {
        super(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + Objects.hashCode(userId);
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

        return Objects.equals(userId, ((FitBitOAuth2AccessToken) obj).getUserId());
    }
}
