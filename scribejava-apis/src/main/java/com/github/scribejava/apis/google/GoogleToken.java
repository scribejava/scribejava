package com.github.scribejava.apis.google;

import com.github.scribejava.core.model.Token;

public class GoogleToken extends Token {

    /**
     * Id_token is part of OpenID Connect specification. It can hold user information that you can directly extract without additional request to
     * provider. See http://openid.net/specs/openid-connect-core-1_0.html#id_token-tokenExample and
     * https://bitbucket.org/nimbusds/nimbus-jose-jwt/wiki/Home
     *
     * Here will be encoded and signed id token in JWT format or null, if not defined.
     */
    private final String openIdToken;

    public GoogleToken(final String token, final String secret, final String rawResponse, final String openIdToken) {
        super(token, secret, rawResponse);
        this.openIdToken = openIdToken;
    }

    @Override
    public String toString() {
        return String.format("GoogleToken{'token'='%s', 'secret'='%s', 'openIdToken'='%s']", getToken(), getSecret(),
                openIdToken);
    }
}
