package com.github.scribejava.core.oauth2.bearersignature;

import com.github.scribejava.core.model.OAuthRequest;

/**
 * 2.1. Authorization Request Header Field <br>
 * https://tools.ietf.org/html/rfc6750#section-2.1
 */
public class BearerSignatureAuthorizationRequestHeaderField implements BearerSignature {

    protected BearerSignatureAuthorizationRequestHeaderField() {
    }

    private static class InstanceHolder {

        private static final BearerSignatureAuthorizationRequestHeaderField INSTANCE
                = new BearerSignatureAuthorizationRequestHeaderField();
    }

    public static BearerSignatureAuthorizationRequestHeaderField instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        request.addHeader("Authorization", "Bearer " + accessToken);
    }
}
