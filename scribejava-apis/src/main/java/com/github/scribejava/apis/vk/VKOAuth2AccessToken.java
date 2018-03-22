package com.github.scribejava.apis.vk;

import com.github.scribejava.core.model.OAuth2AccessToken;
import java.util.Objects;

public class VKOAuth2AccessToken extends OAuth2AccessToken {

    private static final long serialVersionUID = -3539517142527580499L;

    private final String email;

    public VKOAuth2AccessToken(String accessToken, String email, String rawResponse) {
        this(accessToken, null, null, null, null, email, rawResponse);
    }

    public VKOAuth2AccessToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken,
            String scope, String email, String rawResponse) {
        super(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + Objects.hashCode(email);
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

        return Objects.equals(email, ((VKOAuth2AccessToken) obj).getEmail());
    }
}
