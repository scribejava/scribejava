package com.github.scribejava.core.oauth2.bearersignature;

import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;

/**
 * 2.3. URI Query Parameter<br>
 * https://tools.ietf.org/html/rfc6750#section-2.3
 */
public class BearerSignatureURIQueryParameter implements BearerSignature {
    protected BearerSignatureURIQueryParameter() {
    }

    private static class InstanceHolder {

        private static final BearerSignatureURIQueryParameter INSTANCE = new BearerSignatureURIQueryParameter();
    }

    public static BearerSignatureURIQueryParameter instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken);
    }
}
