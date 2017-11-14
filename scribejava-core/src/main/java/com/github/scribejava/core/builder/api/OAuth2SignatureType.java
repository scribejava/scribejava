package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;

public enum OAuth2SignatureType {
    /**
     * https://tools.ietf.org/html/rfc6750#section-2.1
     */
    BEARER_AUTHORIZATION_REQUEST_HEADER_FIELD {
        @Override
        public void signRequest(String accessToken, OAuthRequest request) {
            request.addHeader("Authorization", "Bearer " + accessToken);
        }

    },
    /**
     * https://tools.ietf.org/html/rfc6750#section-2.3
     */
    BEARER_URI_QUERY_PARAMETER {
        @Override
        public void signRequest(String accessToken, OAuthRequest request) {
            request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken);
        }

    };

    /**
     *
     * @param accessToken accessToken
     * @param request request
     * @deprecated use {@link #signRequest(java.lang.String, com.github.scribejava.core.model.OAuthRequest)}
     */
    @Deprecated
    public void signRequest(OAuth2AccessToken accessToken, OAuthRequest request) {
        signRequest(accessToken == null ? null : accessToken.getAccessToken(), request);
    }

    public abstract void signRequest(String accessToken, OAuthRequest request);
}
