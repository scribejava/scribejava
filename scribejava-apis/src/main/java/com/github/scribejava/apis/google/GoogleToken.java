package com.github.scribejava.apis.google;

import com.github.scribejava.core.model.OAuth2AccessToken;

public class GoogleToken extends OAuth2AccessToken {

    private static final long serialVersionUID = 3075266018377678771L;

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

    public GoogleToken(String token, String rawResponse, String openIdToken) {
        super(token, rawResponse);
        this.openIdToken = openIdToken;
    }

    public String getOpenIdToken() {
        return openIdToken;
    }

    @Override
    public String toString() {
        return String.format("GoogleToken{'token'='%s', 'openIdToken'='%s']", getToken(), openIdToken);
    }
}
