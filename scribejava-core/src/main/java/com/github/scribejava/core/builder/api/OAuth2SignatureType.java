package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureAuthorizationRequestHeaderField;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;

/**
 * @deprecated use {@link com.github.scribejava.core.oauth2.bearersignature.BearerSignature}
 */
@Deprecated
public enum OAuth2SignatureType {
    /**
     * @deprecated use
     * {@link com.github.scribejava.core.oauth2.bearersignature.BearerSignatureAuthorizationRequestHeaderField}
     */
    @Deprecated
    BEARER_AUTHORIZATION_REQUEST_HEADER_FIELD {
        @Override
        public void signRequest(String accessToken, OAuthRequest request) {
            BearerSignatureAuthorizationRequestHeaderField.instance().signRequest(accessToken, request);
        }

    },
    /**
     * @deprecated use {@link com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter}
     */
    @Deprecated
    BEARER_URI_QUERY_PARAMETER {
        @Override
        public void signRequest(String accessToken, OAuthRequest request) {
            BearerSignatureURIQueryParameter.instance().signRequest(accessToken, request);
        }
    };

    public abstract void signRequest(String accessToken, OAuthRequest request);
}
