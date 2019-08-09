package com.github.scribejava.apis.openid;

import com.github.scribejava.core.model.OAuth2AccessToken;
import java.util.Objects;

public class OpenIdOAuth2AccessToken extends OAuth2AccessToken {

    private static final long serialVersionUID = -4534058186528117610L;

    /**
     * Id_token is part of OpenID Connect specification. It can hold user information that you can directly extract
     * without additional request to provider.
     *
     * See http://openid.net/specs/openid-connect-core-1_0.html#id_token-tokenExample and
     * https://bitbucket.org/nimbusds/nimbus-jose-jwt/wiki/Home
     *
     * Here will be encoded and signed id token in JWT format or null, if not defined.
     */
    private final String openIdToken;

    public OpenIdOAuth2AccessToken(String accessToken, String openIdToken, String rawResponse) {
        this(accessToken, null, null, null, null, openIdToken, rawResponse);
    }

    public OpenIdOAuth2AccessToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken,
            String scope, String openIdToken, String rawResponse) {
        super(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
        this.openIdToken = openIdToken;
    }

    public String getOpenIdToken() {
        return openIdToken;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + Objects.hashCode(openIdToken);
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

        return Objects.equals(openIdToken, ((OpenIdOAuth2AccessToken) obj).getOpenIdToken());
    }
}
