package com.github.scribejava.apis.google;

import com.github.scribejava.apis.openid.OpenIdOAuth2AccessToken;

/**
 * @deprecated use generic {@link OpenIdOAuth2AccessToken}
 */
@Deprecated
public class GoogleToken extends OpenIdOAuth2AccessToken {

    private static final long serialVersionUID = -5959403983480821444L;

    public GoogleToken(String accessToken, String openIdToken, String rawResponse) {
        super(accessToken, null, null, null, null, openIdToken, rawResponse);
    }

    public GoogleToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken, String scope,
            String openIdToken, String rawResponse) {
        super(accessToken, tokenType, expiresIn, refreshToken, scope, openIdToken, rawResponse);
    }
}
