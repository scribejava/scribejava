package com.github.scribejava.core.pkce;

import com.github.scribejava.core.oauth.AuthorizationUrlBuilder;

/**
 * @deprecated use new builder pattern {@link AuthorizationUrlBuilder} and it's methods<br>
 * {@link AuthorizationUrlBuilder#getPkce()} and {@link AuthorizationUrlBuilder#build()}
 *
 */
@Deprecated
public class AuthorizationUrlWithPKCE {

    private final PKCE pkce;
    private final String authorizationUrl;

    /**
     *
     * @param pkce pkce
     * @param authorizationUrl authorizationUrl
     *
     * @deprecated use new builder pattern {@link AuthorizationUrlBuilder} and it's methods<br>
     * {@link AuthorizationUrlBuilder#getPkce()} and {@link AuthorizationUrlBuilder#build()}
     */
    @Deprecated
    public AuthorizationUrlWithPKCE(PKCE pkce, String authorizationUrl) {
        this.pkce = pkce;
        this.authorizationUrl = authorizationUrl;
    }

    /**
     * @return pkce
     *
     * @deprecated use new builder pattern {@link AuthorizationUrlBuilder} and it's method<br>
     * {@link AuthorizationUrlBuilder#getPkce()}
     */
    @Deprecated
    public PKCE getPkce() {
        return pkce;
    }

    /**
     *
     * @return url
     *
     * @deprecated use new builder pattern {@link AuthorizationUrlBuilder} and it's method<br>
     * {@link AuthorizationUrlBuilder#build()}
     */
    @Deprecated
    public String getAuthorizationUrl() {
        return authorizationUrl;
    }
}
