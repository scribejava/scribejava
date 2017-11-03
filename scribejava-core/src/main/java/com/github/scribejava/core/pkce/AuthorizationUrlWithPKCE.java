package com.github.scribejava.core.pkce;

public class AuthorizationUrlWithPKCE {

    private final PKCE pkce;
    private final String authorizationUrl;

    public AuthorizationUrlWithPKCE(PKCE pkce, String authorizationUrl) {
        this.pkce = pkce;
        this.authorizationUrl = authorizationUrl;
    }

    public PKCE getPkce() {
        return pkce;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

}
